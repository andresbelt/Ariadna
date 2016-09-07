package com.oncreate.ariadna.UI.Quiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.Dialog.LoadingView;
import com.oncreate.ariadna.ImageManager;
import com.oncreate.ariadna.ModelsVO.Quiz;
import com.oncreate.ariadna.R;

import java.util.List;

public class ImageChoiceQuiz extends MultipleChoiceQuiz {
    private ListAdapter adapter;
    private LoadingView loadingView;

    class C06651 extends ArrayAdapter<String> {


        class C12861 implements ImageManager.Listener {
            final /* synthetic */ ImageView val$view;

            C12861(ImageView imageView) {
                this.val$view = imageView;
            }

            public void onResult(Bitmap result) {
                if (result != null) {
                    ImageChoiceQuiz.this.decrementLoading();
                    this.val$view.setImageBitmap(result);
                    return;
                }
                ImageChoiceQuiz.this.setError();
            }
        }

        C06651(Context x0, int x1, List x2) {
            super(x0, x1, x2);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView view;
            if (convertView == null) {
                view = (ImageView) LayoutInflater.from(getContext()).inflate(ImageChoiceQuiz.this.getItemLayout(), parent, false);
            } else {
                view = (ImageView) convertView;
            }
            int fileId = Integer.parseInt((String) getItem(position));
            ImageChoiceQuiz.this.incrementLoading();
            AriadnaApplication.getInstance().getImageManager().getImage(fileId, new C12861(view));
            return view;
        }
    }

    public ImageChoiceQuiz(Context context) {
        super(context);
    }

    protected int getLayout() {
        return R.layout.quiz_image_choice;
    }

    protected int getItemLayout() {
        return R.layout.quiz_image_choice_item;
    }

    protected void onPostCreateQuiz(View view) {
        super.onPostCreateQuiz(view);
        this.loadingView = (LoadingView) view.findViewById(R.id.loading_view);
    }

    public LoadingView getLoadingView() {
        return this.loadingView;
    }

    protected void onShowContent() {
        super.onShowContent();
        this.listView.setVisibility(0);
    }

    protected void onHideContent() {
        super.onHideContent();
        this.listView.setVisibility(4);
    }

    protected void onRetry() {
        this.listView.setAdapter(null);
        this.listView.setAdapter(this.adapter);
    }

    protected ListAdapter createAdapter(Quiz quiz, List<String> answers) {
        this.adapter = new C06651(getContext(), getItemLayout(), answers);
        return this.adapter;
    }

    public void check() {
        if (this.loadingView.getMode() == 0) {
            super.check();
        }
    }
}
