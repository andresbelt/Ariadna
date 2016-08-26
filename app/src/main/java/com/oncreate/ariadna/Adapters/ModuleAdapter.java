package com.oncreate.ariadna.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oncreate.ariadna.Base.AriadnaApplication;
import com.oncreate.ariadna.ImageManager;
import com.oncreate.ariadna.ModelsVO.Module;
import com.oncreate.ariadna.ModelsVO.ModuleState;
import com.oncreate.ariadna.R;

import java.util.ArrayList;
import java.util.List;

public class ModuleAdapter extends Adapter<android.support.v7.widget.RecyclerView.ViewHolder> {
    public static final int CERTIFICATE = 6;
    public static final int MODULE = 0;
    public static final int MODULE_CENTER = 2;
    public static final int MODULE_LEFT = 4;
    public static final int MODULE_NONE = 1;
    public static final int MODULE_RIGHT = 3;
    public static final int SHORTCUT = 5;
    private Context context;
    private int courseId;
    private ArrayList<Object> items;
    private Listener listener;

    public interface Listener {


        void loadModule(Module module, ModuleState moduleState);

        void loadShortcut(Module module);
    }

    private class ModuleAction {
        public long id;
        public Module module;
        public int type;

        private ModuleAction() {
        }
    }



    public class ShortcutViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private Module module;

        /* renamed from: com.sololearn.app.adapters.ModuleAdapter.ShortcutViewHolder.1 */
        class C04971 implements OnClickListener {
            final /* synthetic */ ModuleAdapter val$this$0;

            C04971(ModuleAdapter moduleAdapter) {
                this.val$this$0 = moduleAdapter;
            }

            public void onClick(View view) {
                if (ModuleAdapter.this.listener != null) {
                    ModuleAdapter.this.listener.loadShortcut(ShortcutViewHolder.this.module);
                }
            }
        }

        public ShortcutViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.shortcut_button).setOnClickListener(new C04971(ModuleAdapter.this));
        }

        public void bind(ModuleAction action) {
            this.module = action.module;
            if (AriadnaApplication.getInstance().getProgressManager().getModuleState(action.module.getId()).getState() == 0) {
                this.itemView.setVisibility(ModuleAdapter.MODULE);
                this.itemView.getLayoutParams().height = -2;
                return;
            }
            this.itemView.setVisibility(8);
            this.itemView.getLayoutParams().height = ModuleAdapter.MODULE;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout circle;
        private TextView counts;
        private ImageView icon;
        private int iconId;
        private Module module;
        private TextView name;
        private ProgressBar progressBar;
        private ModuleState state;

        /* renamed from: com.sololearn.app.adapters.ModuleAdapter.ViewHolder.1 */
        class C04981 implements OnClickListener {
            final /* synthetic */ ModuleAdapter val$this$0;

            C04981(ModuleAdapter moduleAdapter) {
                this.val$this$0 = moduleAdapter;
            }

            public void onClick(View view) {
                if (ModuleAdapter.this.listener != null) {
                    ModuleAdapter.this.listener.loadModule(ViewHolder.this.module, ViewHolder.this.state);
                }
            }
        }

        /* renamed from: com.sololearn.app.adapters.ModuleAdapter.ViewHolder.2 */
        class C11482 implements ImageManager.Listener {
            final /* synthetic */ int val$loadIconId;

            C11482(int i) {
                this.val$loadIconId = i;
            }

            public void onResult(Bitmap result) {
                if (this.val$loadIconId == ViewHolder.this.iconId) {
                    ViewHolder.this.icon.setImageBitmap(result);
                }
            }
        }

        public ViewHolder(View itemView) {
            super(itemView);
            this.circle = (FrameLayout) itemView.findViewById(R.id.module_circle);
            this.icon = (ImageView) itemView.findViewById(R.id.module_icon);
            this.name = (TextView) itemView.findViewById(R.id.module_name);
            this.counts = (TextView) itemView.findViewById(R.id.module_counts);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.module_progress);
            itemView.setOnClickListener(new C04981(ModuleAdapter.this));
        }

        public void bind(Module module) {
            boolean z = true;
            this.module = module;
            AriadnaApplication app = AriadnaApplication.getInstance();
            this.state = app.getProgressManager().getModuleState(module.getId());
            switch (this.state.getState()) {
                case ModuleAdapter.MODULE :
                    this.circle.setBackgroundResource(R.drawable.module_disabled);
                    break;
                case ModuleAdapter.MODULE_NONE :
                    this.circle.setBackgroundResource(R.drawable.module_normal);
                    break;
                case ModuleAdapter.MODULE_CENTER :
                    this.circle.setBackgroundResource(R.drawable.module_active);
                    break;
            }
            this.name.setText(module.getName());
            if (this.state.getTotalLessons() <= 0 || this.state.getState() == ModuleAdapter.MODULE_NONE) {
                this.counts.setVisibility(View.GONE);
            } else {
                TextView textView = this.counts;
                Context access$300 = ModuleAdapter.this.context;
                Object[] objArr = new Object[ModuleAdapter.MODULE_CENTER];
                objArr[ModuleAdapter.MODULE] = Integer.valueOf(this.state.getCompletedLessons());
                objArr[ModuleAdapter.MODULE_NONE] = Integer.valueOf(this.state.getTotalLessons());
                textView.setText(access$300.getString(R.string.lesson_number_format, objArr));
                this.counts.setVisibility(ModuleAdapter.MODULE);
            }
            if (this.state.getState() == ModuleAdapter.MODULE_CENTER) {
                this.progressBar.setProgress((this.state.getCompletedItems() * 100) / this.state.getTotalItems());
                this.progressBar.setVisibility(View.VISIBLE);
            } else {
                this.progressBar.setVisibility(View.GONE);
            }
            if (VERSION.SDK_INT >= 21) {
               this.circle.setElevation((float) ((this.state.getState() * ModuleAdapter.MODULE_LEFT) + ModuleAdapter.MODULE_CENTER));
            }
            this.iconId = (module.getId() * 10) + this.state.getState();
            int loadIconId = this.iconId;
            this.icon.setImageBitmap(null);
            ImageManager imageManager = app.getImageManager();
            int access$400 = ModuleAdapter.this.courseId;
            int id = module.getId();
            if (this.state.getState() != 0) {
                z = false;
            }
            imageManager.getModule(access$400, id, z, new C11482(loadIconId));
        }
    }

    public ModuleAdapter(Context context, int courseId, List<Module> modules) {
        this.context = context;
        this.courseId = courseId;
        setHasStableIds(true);
        setItems(modules);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<Module> modules) {
        int i;
        ArrayList<Object> oldItems = this.items;
        this.items = new ArrayList();
        Module module = null;
        for (i = MODULE; i < modules.size(); i += MODULE_NONE) {
            module = (Module) modules.get(i);
            if (i > 0 && module.getAllowShortcut()) {
                ModuleAction shortcut = new ModuleAction();
                shortcut.id = ((long) module.getId()) + 10000000000L;
                shortcut.module = module;
                shortcut.type = SHORTCUT;
                this.items.add(shortcut);
            }
            this.items.add(module);
        }
//        if (module != null) {
//            ModuleAction certificate = new ModuleAction();
//            certificate.id = 10000000000L;
//            certificate.module = module;
//            certificate.type = CERTIFICATE;
//            this.items.add(certificate);
//        }
        if (oldItems == null || oldItems.size() != this.items.size()) {
            notifyDataSetChanged();
            return;
        }
        for (i = MODULE; i < this.items.size(); i += MODULE_NONE) {
            notifyItemChanged(i);
        }
    }

    public List<Object> getItems() {
        return this.items;
    }

    public long getItemId(int i) {
        Object item = this.items.get(i);
        if (item instanceof Module) {
            return (long) ((Module) item).getId();
        }
        return ((ModuleAction) item).id;
    }

    public int getItemCount() {
        return this.items.size();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layout = MODULE;
        switch (viewType) {
            case MODULE /*0*/:
                layout = R.layout.view_module;
                break;
            case SHORTCUT /*5*/:
                layout = R.layout.view_shortcut;
                break;
        }
        View view = LayoutInflater.from(this.context).inflate(layout, viewGroup, false);
        switch (viewType) {
            case MODULE /*0*/:
                return new ViewHolder(view);
            case SHORTCUT /*5*/:
                return new ShortcutViewHolder(view);
            default:
                return null;
        }
    }

    public void onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder viewHolder, int i) {
        Object item = this.items.get(i);
        if (item instanceof Module) {
            ((ViewHolder) viewHolder).bind((Module) item);
        } else if (viewHolder instanceof ShortcutViewHolder) {
            ((ShortcutViewHolder) viewHolder).bind((ModuleAction) item);
        }
    }

    public int getItemViewType(int i) {
        Object item = this.items.get(i);
        if (item instanceof Module) {
            return MODULE;
        }
        return ((ModuleAction) item).type;
    }

    public int getItemType(int i) {

        if (items.get(i) instanceof Module) {
            Module item = (Module) items.get(i);
            switch (item.getAlignment()) {
                case MODULE /*0*/:
                    return MODULE_NONE;
                case MODULE_NONE /*1*/:
                    return MODULE_CENTER;
                case MODULE_CENTER /*2*/:
                    return MODULE_LEFT;
                case MODULE_RIGHT /*3*/:
                    return MODULE_RIGHT;
            }
        }
        if (items.get(i) instanceof ModuleAction) {
            ModuleAction item = (ModuleAction) items.get(i);
            return item.type;
        }
        return MODULE_NONE;
    }
}
