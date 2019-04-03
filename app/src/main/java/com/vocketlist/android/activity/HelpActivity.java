package com.vocketlist.android.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.vocketlist.android.R;
import com.vocketlist.android.adapter.HelpAdapter;
import com.vocketlist.android.adapter.viewholder.ListItemHelp;
import com.vocketlist.android.decoration.DividerInItemDecoration;
import com.vocketlist.android.util.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 문의/도움말
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 13.
 */
public class HelpActivity extends DepthBaseActivity {
    private static final String TAG = HelpActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.tvMailTo)
    AutoLinkTextView tvMailTo;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @BindString(R.string.help_question_email) String email;
    @BindString(R.string.help_question_email_subject) String emailSubject;
    @BindString(R.string.help_question_email_content) String emailContent;
    @BindArray(R.array.help_titles) String[] titles;
    @BindArray(R.array.help_contents) String[] contents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // 사용중인 기기 / 운영체제 버전 / 보킷리스트 버전
        final String content = String.format(Locale.getDefault(), emailContent, Build.BRAND + " " + Build.MODEL, String.valueOf(Build.VERSION.RELEASE), Version.getCurrentVersionName(this));

        // 이메일
        tvMailTo.addAutoLinkMode(AutoLinkMode.MODE_EMAIL);
        tvMailTo.setAutoLinkText(getString(R.string.help_question_with_email));
        tvMailTo.setAutoLinkOnClickListener((autoLinkMode, matchedText) -> {
            if(autoLinkMode == AutoLinkMode.MODE_EMAIL && matchedText.equals(email)) {
                // 메일앱 런칭
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
                    .putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                    .putExtra(Intent.EXTRA_TEXT, content));
            }
        });

        // 레이아웃 : 라사이클러
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        recyclerView.addItemDecoration(new DividerInItemDecoration(this, lm.getOrientation())); // 구분선
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        //
        initData();
    }

    /**
     * 초기화 : 데이터
     */
    private void initData() {
        List<ListItemHelp> item = new ArrayList<>();
        for(int i = 0; i < titles.length; i++) {
            item.add(new ListItemHelp(String.format(Locale.getDefault(), titles[i], i + 1), contents[i]));
        }

        recyclerView.setAdapter(new HelpAdapter(item));
    }
}
