package com.oncreate.ariadna.UI.Quiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.volley.DefaultRetryPolicy;
import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.ImageManager;
import com.oncreate.ariadna.R;
import com.oncreate.ariadna.UI.views.SquareRelativeLayout;


public class ImagePlaceholderQuiz extends PlaceholderQuiz {
    private boolean arePlaceholdersAspected;
    private SparseArray<Float> aspectArray;
    private float imageScale;

    private class DragManagerr extends DragManager {
        private OnClickListener scaledClick;
        private View scaledImage;
        private int scaledImageX;
        private int scaledImageY;

        /* renamed from: com.sololearn.app.views.quizzes.ImagePlaceholderQuiz.DragManager.1 */
        class C06671 implements OnClickListener {
            C06671() {
            }

            public void onClick(View v) {
                DragManagerr.this.closeCurrentImage();
            }
        }

        /* renamed from: com.sololearn.app.views.quizzes.ImagePlaceholderQuiz.DragManager.2 */
        class C06682 implements Runnable {
            final /* synthetic */ View val$view;

            C06682(View view) {
                this.val$view = view;
            }

            public void run() {
                ImagePlaceholderQuiz.this.overlay.removeView(this.val$view);
                if (DragManagerr.this.scaledImage == this.val$view) {
                    DragManagerr.this.scaledImage = null;
                }
            }
        }

        private DragManagerr() {
            super();
            this.scaledClick = new C06671();
        }

        private void closeCurrentImage() {
            if (this.scaledImage != null) {
                this.scaledImage.setOnClickListener(null);
                View view = this.scaledImage;
                ViewCompat.animate(view).scaleX(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).scaleY(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT).translationXBy((float) (-this.scaledImageX)).translationYBy((float) (-this.scaledImageY)).setDuration(150).withEndAction(new C06682(view));
            }
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            closeCurrentImage();
            return super.onTouch(view, motionEvent);
        }

        protected void onClick(View view) {
            closeCurrentImage();
            Item item = (Item) view.getTag();
            if (item != null) {
                Rect overlayRect = getViewRect(ImagePlaceholderQuiz.this.overlay, null);
                Rect itemRect = getViewRect(item.getView(), overlayRect);
                int width = itemRect.width();
                int shift = (((int) (((float) width) * ImagePlaceholderQuiz.this.imageScale)) - width) / 2;
                this.scaledImageX = Math.max(shift - itemRect.left, 0);
                if (itemRect.right + shift > overlayRect.right) {
                    this.scaledImageX = overlayRect.right - (itemRect.right + shift);
                }
                this.scaledImageY = (int) ((((float) itemRect.height()) * (DefaultRetryPolicy.DEFAULT_BACKOFF_MULT - ImagePlaceholderQuiz.this.imageScale)) / 2.0f);
                this.scaledImage = ImagePlaceholderQuiz.this.createItem(ImagePlaceholderQuiz.this.overlay, item.getContent()).getView();
                this.scaledImage.setTranslationX((float) itemRect.left);
                this.scaledImage.setTranslationY((float) itemRect.top);
                ViewCompat.animate(this.scaledImage).setDuration(200).scaleX(ImagePlaceholderQuiz.this.imageScale).scaleY(ImagePlaceholderQuiz.this.imageScale).translationXBy((float) this.scaledImageX).translationYBy((float) this.scaledImageY);
                this.scaledImage.setOnClickListener(this.scaledClick);
                ImagePlaceholderQuiz.this.overlay.addView(this.scaledImage);
            }
        }
    }

    private class Iteme extends Item {
        private SquareRelativeLayout container;
        private ImageView imageView;

        /* renamed from: com.sololearn.app.views.quizzes.ImagePlaceholderQuiz.Item.1 */
        class C12871 implements ImageManager.Listener {
            final /* synthetic */ int val$fileId;

            C12871(int i) {
                this.val$fileId = i;
            }

            public void onResult(Bitmap result) {
                if (result != null) {
                    Iteme.this.imageView.setImageBitmap(result);
                    float aspect = (((float) result.getWidth()) * DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) / ((float) result.getHeight());
                    ImagePlaceholderQuiz.this.aspectArray.put(this.val$fileId, Float.valueOf(aspect));
                    Iteme.this.container.setAspectRatio(aspect);
                    ImagePlaceholderQuiz.this.decrementLoading();
                    return;
                }
                ImagePlaceholderQuiz.this.setError();
            }
        }

        public Iteme(Context context, ViewGroup parent, String content) {
            super(context, parent, 0.0f, content);
        }

        protected View createView(Context context, ViewGroup parent, float textSize, String placeholder) {
            View view = LayoutInflater.from(context).inflate(R.layout.quiz_image_placeholder_item, parent, false);
            this.container = (SquareRelativeLayout) view.findViewById(R.id.container);
            this.imageView = (ImageView) view.findViewById(R.id.image);
            int fileId = Integer.parseInt(placeholder);
            ImagePlaceholderQuiz.this.incrementLoading();
            AriadnaApplication.getInstance().getImageManager().getImage(fileId, new C12871(fileId));
            return view;
        }

        public void setEnabled(boolean enabled) {
            this.isEnabled = enabled;
            ViewCompat.animate(getView()).alpha(enabled ? DefaultRetryPolicy.DEFAULT_BACKOFF_MULT : 0.1f).setDuration(10);
        }
    }

    private class Placeholdere extends Placeholder {
        private SquareRelativeLayout container;
        private ImageView imageView;

        /* renamed from: com.sololearn.app.views.quizzes.ImagePlaceholderQuiz.Placeholder.1 */
        class C12881 implements ImageManager.Listener {
            C12881() {
            }

            public void onResult(Bitmap result) {
                Placeholdere.this.imageView.setImageBitmap(result);
            }
        }

        public Placeholdere(Context context, ViewGroup parent, String placeholder) {
            super(context, parent, 0.0f, placeholder);
        }

        protected View createView(Context context, ViewGroup parent, float textSize, String placeholder) {
            View view = LayoutInflater.from(context).inflate(R.layout.quiz_image_placeholder_container, parent, false);
            this.container = (SquareRelativeLayout) view.findViewById(R.id.container);
            this.imageView = (ImageView) view.findViewById(R.id.image);
            return view;
        }

        public void setCandidate(Item item) {
        }

        protected void setContent(String content) {
            if (content != null) {
                AriadnaApplication.getInstance().getImageManager().getImage(Integer.parseInt(content), new C12881());
                return;
            }
            this.imageView.setImageBitmap(null);
        }

        public void setAspect(float aspect) {
            this.container.setAspectRatio(aspect);
        }

    }

    public ImagePlaceholderQuiz(Context context) {
        super(context);
        this.aspectArray = new SparseArray();
        this.arePlaceholdersAspected = false;
        this.imageScale = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        this.imageScale = getResources().getDimension(R.dimen.quiz_image_placeholder_item_scaled_size) / getResources().getDimension(R.dimen.quiz_image_placeholder_item_size);
    }

    protected Item createItem(ViewGroup parent, String content) {
        return new Iteme(getContext(), parent, content);
    }

    protected Placeholder createPlaceholder(ViewGroup parent, String placeholder) {
        return new Placeholdere(getContext(), parent, placeholder);
    }

    protected DragManager createDragManager() {
        return new DragManagerr();
    }

    protected void onShowContent() {
        super.onShowContent();
        if (!this.arePlaceholdersAspected && this.placeholders != null) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                Placeholdere placeholder = (Placeholdere) this.placeholders.valueAt(i);
                placeholder.setAspect(((Float) this.aspectArray.get(Integer.parseInt(placeholder.getPlaceholder()))).floatValue());
            }
            this.arePlaceholdersAspected = true;
        }
    }
}
