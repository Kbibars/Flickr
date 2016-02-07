package kbibars.com.flickr;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import Utility.Url;
import Utility.RecyclerViewAdapter;
import Utility.SingleResponse;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends ActionBarActivity {
/*Notes* MainActivity =1 UserActivity =2 */

    /*Statics*/
    private static final String RESPONSE_TAG_PHOTO = "photo";
    private static final String RESPONSE_ATTR_ID = "id";
    private static final String RESPONSE_ATTR_SECRET = "secret";
    private static final String RESPONSE_ATTR_SERVER = "server";
    private static final String RESPONSE_ATTR_FARM = "farm";
    private static final String RESPONSE_ATTR_TITLE = "title";
    private static final String RESPONSE_ATTR_OWNER = "owner";

    /*Declarations*/
    private ProgressBar progressBar = null;
    private RecyclerView mRecyclerView = null;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout activity_linear_layout = null;
    public ArrayList<SingleResponse> mSingleResponse = null;
    public ProgressDialog dialog = null;
    public EditText mSearchTextView = null;
    public Button mImageViewButton = null;
    public SharedPreferences prefs = null;
    public int colour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Definitions & Initializations*/
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mSearchTextView = (EditText) findViewById(R.id.mSearchTextview);
        mImageViewButton = (Button) findViewById(R.id.button);
        progressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        activity_linear_layout = (LinearLayout) findViewById(R.id.activity_linear_layout);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        /*Handling the RecyclerView*/
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*Initializing the current View*/
        initView();

        mImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mSearchTextView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                /*Check if the keyword has been used before*/
                String cacheChecker = prefs.getString(mSearchTextView.getText().toString(), "NSF");/*No String Found*/
                /*Call the method to fetch data from the URL*/
                if (cacheChecker.equals("NSF"))
                    fetchData(mSearchTextView.getText());
                else {
                    try {
               /*Call a method to load data from the cached data*/
                        loadAdapter(cacheChecker);
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    private void fetchData(final Editable mSearchText) {
        AsyncHttpClient client = new AsyncHttpClient();
        Url const_var = new Url();
        /*Use AsyncHttpClient to call the constructed URL and get response from the server in XML fotmat*/
        client.post(getApplicationContext(), "https://api.flickr.com/services/rest/?method=" + const_var.getMethod() + "&api_key=" + const_var.getApi_key() + "&format=" + const_var.getFormat()
                        + "&text=" + mSearchText + "&per_page=500",
                null, new TextHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        dialog = ProgressDialog.show(MainActivity.this, "", "Loading...", true);
                        dialog.setCancelable(true);
                        // progressBar.setVisibility(View.VISIBLE);
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Could not connect to server", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {

                        /*Save  the responseString in shared preferences */
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(mSearchText.toString(), responseString);
                        editor.apply();

                        try {
                            /*Call a method to load data from the responseString*/
                            loadAdapter(responseString);
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                        super.onFinish();
                        //  progressBar.setVisibility(View.GONE);
                    }

                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.change_color) {
            changeColor();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*XML Parser Method to retrieve photos*/
    private ArrayList<SingleResponse> parsePhotos(XmlPullParser parser, ArrayList<SingleResponse> responseList)
            throws XmlPullParserException, IOException {
        int type;
        String name;
        final int depth = parser.getDepth();
        /*Parse the XML and save each image data in an arraylist of singleResponse*/
        while (((type = parser.next()) != XmlPullParser.END_TAG ||
                parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
            if (type != XmlPullParser.START_TAG) {
                continue;
            }
            name = parser.getName();
            if (RESPONSE_TAG_PHOTO.equals(name)) {
                final SingleResponse singleResponse = new SingleResponse();
                singleResponse.id = parser.getAttributeValue(null, RESPONSE_ATTR_ID);
                singleResponse.secret = parser.getAttributeValue(null, RESPONSE_ATTR_SECRET);
                singleResponse.server = parser.getAttributeValue(null, RESPONSE_ATTR_SERVER);
                singleResponse.farm = parser.getAttributeValue(null, RESPONSE_ATTR_FARM);
                singleResponse.title = parser.getAttributeValue(null, RESPONSE_ATTR_TITLE);
                singleResponse.owner = parser.getAttributeValue(null, RESPONSE_ATTR_OWNER);

                responseList.add(singleResponse);
            }
        }

        return responseList;
    }

    public void changeColor() {
        /*Change the colour of mRecyclerView background*/
        colour = prefs.getInt("colour", 0);
        if (colour == 0) {
            activity_linear_layout.setBackgroundResource(R.drawable.background);
            mRecyclerView.setBackgroundResource(R.drawable.background);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("colour", 1);
            editor.apply();
        } else if (colour == 1) {
            activity_linear_layout.setBackgroundResource(R.drawable.background_2_);
            mRecyclerView.setBackgroundResource(R.drawable.background_2_);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("colour", 0);
            editor.apply();
        }
    }

    public void setcolor() {
        /*Change the colour of mRecyclerView background*/
        colour = prefs.getInt("colour", 0);
        if (colour == 0) {
            activity_linear_layout.setBackgroundResource(R.drawable.background_2_);
            mRecyclerView.setBackgroundResource(R.drawable.background_2_);
        } else if (colour == 1) {
            activity_linear_layout.setBackgroundResource(R.drawable.background);
            mRecyclerView.setBackgroundResource(R.drawable.background);
        }
    }

    public void loadAdapter(String responseString) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(responseString));

        ArrayList<SingleResponse> singleResponses = parsePhotos(xpp, new ArrayList<SingleResponse>());

        mAdapter = new RecyclerViewAdapter(singleResponses, getApplicationContext(), 1);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);


    }

    public void initView() {
        setcolor();
        /*Hiding the Keyboard @AMainActivity Creation*/
        progressBar.setVisibility(View.GONE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }
}