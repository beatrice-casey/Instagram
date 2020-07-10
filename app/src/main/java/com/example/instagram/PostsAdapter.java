package com.example.instagram;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    private boolean isLiked;
    public static final String TAG = "Adapter";

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvTimestamp;
        private Button btnLike;
        final ParseUser currentUser;
        private Like like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            btnLike = itemView.findViewById(R.id.btnLike);
            currentUser = ParseUser.getCurrentUser();
            like = new Like();
            itemView.setOnClickListener(this);
        }

        public void bind(final Post post) {
            //bind data into view elements
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvTimestamp.setText(DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime(),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            }
            checkLiked();
            if (isLiked) {
                btnLike.setBackgroundResource(R.drawable.ic_like_fill);
            }

            //like onClicklistener
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkLiked();
                    if (isLiked == false) {
                       btnLike.setBackgroundResource(R.drawable.ic_like_fill);
                       isLiked = true;
                       post.saveLike(currentUser);
                       post.saveInBackground(new SaveCallback() {
                           @Override
                           public void done(ParseException e) {
                               if (e != null) {
                                   Log.e(TAG, "Error saving post", e);
                                   return;
                               }
                           }
                       });
                    } else {
                        btnLike.setBackgroundResource(R.drawable.ic_like);
                        isLiked = false;
                        post.deleteLike(like);
                        post.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Error saving post", e);
                                    return;
                                }
                            }
                        });
                    }


                }
            });

        }

        private void checkLiked() {
            ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
            query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
            query.whereEqualTo(Like.KEY_POST, posts.get(getAdapterPosition()));
            query.findInBackground(new FindCallback<Like>() {
                @Override
                public void done(List<Like> likes, ParseException e) {
                    if (e != null) {
                        return;
                    }
                    if (likes.isEmpty()) {
                        Log.i(TAG, "likes list is empty.");
                        isLiked = false;
                    } else {
                        Log.i(TAG, "likes list is not empty.");
                        isLiked = true;
                        like = likes.get(0);
                    }

                }
            });
        }

        @Override
        public void onClick(View view) {
            //getting adapter position
            int position = getAdapterPosition();
            //make sure position is valid (it exists in view)
            if (position != RecyclerView.NO_POSITION) {
                //get the movie at that position
                Post post = posts.get(position);
                //make an intent to display MovieDetailsActivity
                Intent intent = new Intent(context, PostDetailsActivity.class);
                //serialize the movie using parceler, use short name as key
                intent.putExtra("post", post);
                //show the activity
                context.startActivity(intent);
            }
        }

    }
    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

}
