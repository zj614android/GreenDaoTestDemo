package demo.greendaotest.com.greendaotestdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DaoMaster.DevOpenHelper devOpenHelper;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private UserDao mUserDao;
    private TextView mContentTv;

    private String action = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContentTv = (TextView) findViewById(R.id.content_tv);

        initGreenDao();


        initClick();
    }

    private void initClick() {
        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.del).setOnClickListener(this);
        findViewById(R.id.edit).setOnClickListener(this);
        findViewById(R.id.query).setOnClickListener(this);
    }


    /**
     * 初始化
     */
    private void initGreenDao() {
        devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "zj.db", null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
        mUserDao = daoSession.getUserDao();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.add:
                add();
                break;
            case R.id.del:
                del();
                break;
            case R.id.edit:
                edit();
                break;
            case R.id.query:
                query();
                break;
        }

        refreshUi();
    }

    /**
     * 查询
     */
    private void query() {
        action = "query";

        List<User> list = mUserDao.queryBuilder()
                .where(UserDao.Properties.Id.between(2, 13)).limit(5).build().list();
        for (int i = 0; i < list.size(); i++) {
            Log.d("google_lenve", "search: " + list.get(i).toString());
        }

//        List<User> list = mUserDao.queryBuilder().list();

        showContentTextView(list);
    }

    /**
     * 修改
     */
    private void edit() {
        action = "edit";

//        User user = mUserDao.queryBuilder()
//                .where(UserDao.Properties.Id.ge(10), UserDao.Properties.Username.like("%90%")).build().unique();
//        if (user == null) {
//            Toast.makeText(MainActivity.this, "用户不存在!", Toast.LENGTH_SHORT).show();
//        }else{
//            user.setUsername("王五");
//        }

        User user = mUserDao.queryBuilder()
                .where(UserDao.Properties.Id.eq(5)).build().unique();

        if(null != user){
            user.setName("王尼玛哇哈哈");
            mUserDao.update(user);
        }else {
            Toast.makeText(MainActivity.this, "用户不存在!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 删除
     */
    private void del() {
        action = "del";
        mUserDao.deleteAll();
    }

    /**
     * 添加
     */
    private void add() {
        action = "add";
        Random rd = new Random();
        int ranDomNum = rd.nextInt(26626);
        User user = new User(null, "贾君鹏" + ranDomNum);
        mUserDao.insert(user);
    }


    /**
     * 刷新UI
     */
    private void refreshUi() {

        if (action.equals("query")) {
            return;
        }

        List<User> list = mUserDao.queryBuilder().list();
        showContentTextView(list);
    }

    /**
     * 显示内容字
     * @param list
     */
    private void showContentTextView(List<User> list) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            if (null != user) {
                sb.append(user.toString() + "\n");
            }
        }

        String text = sb.toString();

        if (TextUtils.isEmpty(text)) {
            text = "null";
        }

        mContentTv.setText(action + " _ \n " + text);
    }

}
