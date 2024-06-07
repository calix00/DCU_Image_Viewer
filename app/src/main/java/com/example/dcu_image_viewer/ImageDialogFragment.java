package com.example.dcu_image_viewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ImageDialogFragment extends DialogFragment {

    private static final String ARG_IMAGES = "images";
    private static final String ARG_POSITION = "position";

    private List<String> imagePaths;
    private int position;

    public static ImageDialogFragment newInstance(List<String> imagePaths, int position) {
        ImageDialogFragment fragment = new ImageDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_IMAGES, new ArrayList<>(imagePaths));
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imagePaths = getArguments().getStringArrayList(ARG_IMAGES);
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_dialog, container, false);

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ImagePagerAdapter(imagePaths));
        viewPager.setCurrentItem(position, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ThumbnailAdapter(imagePaths, position -> {
            viewPager.setCurrentItem(position, true);
        }));

        return view;
    }

    private static class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ViewHolder> {

        private List<String> imagePaths;

        ImagePagerAdapter(List<String> imagePaths) {
            this.imagePaths = imagePaths;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pager_image, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String imagePath = imagePaths.get(position);
            Glide.with(holder.itemView.getContext()).load(imagePath).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return imagePaths.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }

    private static class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ViewHolder> {

        private List<String> imagePaths;
        private OnThumbnailClickListener listener;

        ThumbnailAdapter(List<String> imagePaths, OnThumbnailClickListener listener) {
            this.imagePaths = imagePaths;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false);
            return new ViewHolder(view, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String imagePath = imagePaths.get(position);
            Glide.with(holder.itemView.getContext()).load(imagePath).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return imagePaths.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            ViewHolder(View itemView, OnThumbnailClickListener listener) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                itemView.setOnClickListener(v -> listener.onThumbnailClick(getAdapterPosition()));
            }
        }

        interface OnThumbnailClickListener {
            void onThumbnailClick(int position);
        }
    }
}
