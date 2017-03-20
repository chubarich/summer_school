package com.luseen.yandexsummerschool.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.luseen.yandexsummerschool.R;
import com.luseen.yandexsummerschool.base_mvp.api.ApiFragment;
import com.luseen.yandexsummerschool.ui.widget.CloseIcon;
import com.luseen.yandexsummerschool.ui.widget.TranslationView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class TranslationFragment extends ApiFragment<TranslationFragmentContract.View, TranslationFragmentContract.Presenter>
        implements TranslationFragmentContract.View {


    @BindView(R.id.translation_view)
    TranslationView translationView;

    @BindView(R.id.root_layout)
    FrameLayout rootLayout;

    private Subscription textChangeSubscription;
    private CloseIcon closeIcon;

    public static TranslationFragment newInstance() {
        Bundle args = new Bundle();
        TranslationFragment fragment = new TranslationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translation, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: 20.03.2017 enable when text is empty
        translationView.enable();
    }

    @NonNull
    @Override
    public TranslationFragmentContract.Presenter createPresenter() {
        return new TranslationFragmentPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeIcon = translationView.getCloseIcon();
        textChangeSubscription = RxTextView.textChanges(translationView.getTranslationEditText())
                .debounce(500L, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .filter(charSequence -> !charSequence.toString().isEmpty())
                .map(CharSequence::toString)
                .map(String::trim)
                .subscribe(s -> {
                    presenter.handleInputText(s);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (textChangeSubscription != null && !textChangeSubscription.isUnsubscribed()) {
            textChangeSubscription.unsubscribe();
        }
    }

    @Override
    protected boolean whitButterKnife() {
        return true;
    }

    @OnClick(R.id.root_layout)
    public void onClick() {
        if (translationView.isEnable()) {
            translationView.disable();
        }
    }
}
