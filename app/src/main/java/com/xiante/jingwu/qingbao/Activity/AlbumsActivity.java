package com.xiante.jingwu.qingbao.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.Album.ActivityUtil;
import com.xiante.jingwu.qingbao.Album.AlbumItemAdapter;
import com.xiante.jingwu.qingbao.Album.AlbumsAdapter;
import com.xiante.jingwu.qingbao.Album.PhotoUpAlbumHelper;
import com.xiante.jingwu.qingbao.Album.PhotoUpImageBucket;
import com.xiante.jingwu.qingbao.Album.PhotoUpImageItem;
import com.xiante.jingwu.qingbao.MessageEvent.UpdateViewMessage;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.xiante.jingwu.qingbao.Album.AlbumItemAdapter.CODE_TAKE_PHOTO;
import static com.xiante.jingwu.qingbao.Album.AlbumItemAdapter.TEMPLE_IMAGE;

public class AlbumsActivity extends Activity {

    ImageView backTV;
    @InjectView(R.id.titlebarRightTV)
    TextView tvRight;
    @InjectView(R.id.titlebarTitleTV)
    TextView tvTitle;
    @InjectView(R.id.showAllImageGV)
    GridView showAllImageGV;
    @InjectView(R.id.showAlbumsBT)
    Button showAlbumsBT;
    @InjectView(R.id.albumsContainer)
    RelativeLayout albumsContainer;
    @InjectView(R.id.album_gridv)
    GridView album_gridv;
    private AlbumsAdapter albumsAdapter;
    private PhotoUpAlbumHelper photoUpAlbumHelper;
    private List<PhotoUpImageBucket> list;
    private AlbumItemAdapter itemsAdapter;
    private ArrayList<PhotoUpImageItem> imagesList;
    private boolean isVisiable = false;
    private ArrayList<PhotoUpImageItem> selectedPhoto;
    private String ImageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CamerImage/";
    private String takePicPath = "";
    private int TakePhotoRequestCode = 0x141, CutPhotoRequesCode = 0x105;
    private Handler handler, intentHandler;
    private Bundle bundle;

    private int maxNum;
    private String inputkey="";
    private int view_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.albums_gridview);
        ButterKnife.inject(this);
        bundle = getIntent().getExtras();
        init();
        loadData();
        onItemClick();
    }


    private void init() {
        backTV=findViewById(R.id.titlebarBackView);
        tvTitle.setText("图片选择");
        tvRight.setVisibility(View.VISIBLE);
        maxNum=Integer.parseInt(getIntent().getStringExtra("maxSelect"));
        inputkey=getIntent().getStringExtra(Global.INPUTKEY);
        view_id=getIntent().getIntExtra(Global.VIEW_ID,0);
        tvRight.setText("上传");
        albumsAdapter = new AlbumsAdapter(AlbumsActivity.this);
        album_gridv.setAdapter(albumsAdapter);
        imagesList = new ArrayList<PhotoUpImageItem>();
        //addTopView();
        selectedPhoto = new ArrayList<PhotoUpImageItem>();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ActivityUtil.setBadgeCount(getApplicationContext(), 3);
            }
        };

        intentHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


            }
        };

    }

    private void loadData() {
        File f = new File(ImageFilePath);
        if (!f.exists()) {
            f.mkdirs();
        }

        photoUpAlbumHelper = PhotoUpAlbumHelper.getHelper();
        photoUpAlbumHelper.init(AlbumsActivity.this);
        photoUpAlbumHelper.setGetAlbumList(new PhotoUpAlbumHelper.GetAlbumList() {
            @Override
            public void getAlbumList(List<PhotoUpImageBucket> list) {

                PhotoUpImageBucket firstbucket = addPersonalImage();
                if(firstbucket!=null){
                    list.add(0,firstbucket);
                }

                AlbumsActivity.this.list = list;
                imagesList.clear();
                for (int i = 0; i < list.size(); i++) {
                    imagesList.addAll(AlbumsActivity.this.list.get(i).getImageList());
                }

                ArrayList<PhotoUpImageItem> templist = new ArrayList<PhotoUpImageItem>();
                templist.addAll(imagesList);

                PhotoUpImageBucket bucket = new PhotoUpImageBucket();
                bucket.setBucketName("所有图片");
                bucket.setCount(imagesList.size());
                bucket.setImageList(templist);
                list.add(list.size(), bucket);
                albumsAdapter.setArrayList(list);
                albumsAdapter.notifyDataSetChanged();

                itemsAdapter = new AlbumItemAdapter(imagesList, AlbumsActivity.this);
                itemsAdapter.setMaxSelect(maxNum);
                showAllImageGV.setAdapter(itemsAdapter);
            }
        });
        photoUpAlbumHelper.execute(false);
    }

    private PhotoUpImageBucket addPersonalImage() {
        PhotoUpImageBucket bucket = new PhotoUpImageBucket();
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+"/"+TEMPLE_IMAGE+"/");
        if (mediaStorageDir.exists()) {
            File[] tempfile=mediaStorageDir.listFiles();
            if(tempfile!=null){
                List<PhotoUpImageItem> photoUpImageItemList=new ArrayList<>();
                for(int i=0;i<tempfile.length;i++){
                    PhotoUpImageItem imageItem=new PhotoUpImageItem();
                    imageItem.setImagePath(tempfile[i].getAbsolutePath());
                     photoUpImageItemList.add(imageItem);
                }

                bucket.setBucketName("TEMPIMAGE");
                bucket.setCount(photoUpImageItemList.size());
                bucket.setImageList(photoUpImageItemList);
            }else {
                return null;
            }

        }else {
            return null;
        }
        return bucket;
    }

    private void onItemClick() {

        backTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        albumsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                album_gridv.setVisibility(View.GONE);
                albumsContainer.setVisibility(View.GONE);
                isVisiable=false;
            }
        });
        album_gridv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                imagesList.clear();
                imagesList.addAll(list.get(position).getImageList());
                itemsAdapter.refreshAdapter();
                showAlbumsVisibility();
                showAlbumsBT.setText(list.get(position).getBucketName());

            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PhotoUpImageItem> selectlist = itemsAdapter.getSelectImage();
                JSONArray array=new JSONArray();
                for (int i = 0; i < selectlist.size(); i++) {
                    array.put(selectlist.get(i).getImagePath());
                }
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("id",view_id);
                    jsonObject.put("value",array.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new UpdateViewMessage(inputkey,jsonObject.toString()));
                finish();
            }
        });
        showAlbumsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlbumsVisibility();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case  CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    loadData();

                }
                break;
        }

    }


    public void showAlbumsVisibility() {
        if (isVisiable) {
            album_gridv.setVisibility(View.GONE);
            albumsContainer.setVisibility(View.GONE);
        } else {
            album_gridv.setVisibility(View.VISIBLE);
            albumsContainer.setVisibility(View.VISIBLE);
        }
        isVisiable = !isVisiable;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
