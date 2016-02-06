package kbibars.com.flickr;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import Utility.Const_Var;
import Utility.MyAdapter;
import Utility.SingleResponse;
import cz.msebera.android.httpclient.Header;

public class User_Activity extends ActionBarActivity {
    /*Declarations*/
    public ProgressBar progressBar = null;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mUSerRecyclerView;
    public String mUserID = null;
    ProgressDialog dialog;
    /*Statics*/
    private static final String RESPONSE_TAG_PHOTO = "photo";
    private static final String RESPONSE_ATTR_ID = "id";
    private static final String RESPONSE_ATTR_SECRET = "secret";
    private static final String RESPONSE_ATTR_SERVER = "server";
    private static final String RESPONSE_ATTR_FARM = "farm";
    private static final String RESPONSE_ATTR_TITLE = "title";
    private static final String RESPONSE_ATTR_OWNER = "owner";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Bundle mBundle = getIntent().getExtras();
        /*FetchExtras and pull the User ID*/
        if (mBundle != null && mBundle.containsKey("mOwnerID")) {
            mUserID = mBundle.getString("mOwnerID");
        }
        /*Checks for the app color */
        changeColor();
            /*Calling the View */
        mUSerRecyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mUSerRecyclerView.setLayoutManager(mLayoutManager);
        progressBar = (ProgressBar) findViewById(R.id.mProgressBar);

                /*Declarations*/
        progressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        fetchData();
    }

    private void fetchData() {
        AsyncHttpClient client = new AsyncHttpClient();
        Const_Var const_var = new Const_Var();
        client.post(getApplicationContext(), "https://api.flickr.com/services/rest/?method=" + const_var.getMethod() + "&api_key=" + const_var.getApi_key() + "&format=" + const_var.getFormat()
                        + "&user_id=" + mUserID + "&per_page=500",
                null, new TextHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        dialog = ProgressDialog.show(User_Activity.this, "", "Loading...", true);
                        dialog.setCancelable(true);
                        progressBar.setVisibility(View.VISIBLE);
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Could not connect to server", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                            factory.setNamespaceAware(true);
                            XmlPullParser xpp = factory.newPullParser();
                            xpp.setInput(new StringReader(responseString));

                            ArrayList<SingleResponse> mylist = parsePhotos(xpp, new ArrayList<SingleResponse>());

                            mAdapter = new MyAdapter(mylist, getApplicationContext(), 1);
                            mAdapter.notifyDataSetChanged();
                            mUSerRecyclerView.setAdapter(mAdapter);

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
                        progressBar.setVisibility(View.GONE);
                    }

                });
    }


    /*XML Parser Method*/
    private ArrayList<SingleResponse> parsePhotos(XmlPullParser parser, ArrayList<SingleResponse> responseList)
            throws XmlPullParserException, IOException {
        int type;
        String name;


        final int depth = parser.getDepth();

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int colour = prefs.getInt("colour", 0);
        if (colour == 1) {
            mUSerRecyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);
            mUSerRecyclerView.setBackgroundResource(R.drawable.background);

        } else if (colour==0) {
            mUSerRecyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);
            mUSerRecyclerView.setBackgroundResource(R.drawable.background_2_);

        }
    }
}


