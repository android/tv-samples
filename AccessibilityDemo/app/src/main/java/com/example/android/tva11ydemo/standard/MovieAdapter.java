package com.example.android.tva11ydemo.standard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat.BadgeIconType;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.tva11ydemo.R;
import com.example.android.tva11ydemo.model.Movie;
import java.util.List;

/**
 * MovieAdapter is the adapter for RecycleView of Movie list.
 */
public final class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

  private final List<Movie> movieList;
  private static boolean hasSetFocus = false;

  static class MovieViewHolder extends RecyclerView.ViewHolder {
    ImageView movieImage;
    TextView movieName;

    public MovieViewHolder(View view) {
      super(view);
      movieImage = (ImageView) view.findViewById(R.id.movie_image);
      movieName = (TextView) view.findViewById(R.id.movie_name);
    }
  }

  public MovieAdapter(List<Movie> movieList) {
    this.movieList = movieList;
  }

  @Override
  public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.standard_movie_item,
        parent, false);
    final MovieViewHolder holder = new MovieViewHolder(view);
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int position = holder.getAdapterPosition();
        Movie movie = movieList.get(position);
        Toast.makeText(v.getContext(),
            "you click movie " + movie.getTitle(), Toast.LENGTH_SHORT).show();
      }
    });
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
    Movie movie = movieList.get(position);
    holder.movieName.setText(movie.getTitle());
    holder.movieImage.setImageResource(R.drawable.movie);
    if (!hasSetFocus) {
      holder.itemView.requestFocus();
      hasSetFocus = true;
    }
  }

  @Override
  public int getItemCount() {
    return movieList.size();
  }
}
