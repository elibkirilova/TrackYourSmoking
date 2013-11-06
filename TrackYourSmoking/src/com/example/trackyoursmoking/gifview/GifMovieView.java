package com.example.trackyoursmoking.gifview;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.View;

public class GifMovieView extends View {

    private Movie mMovie;

    private long mMoviestart;

    public GifMovieView(Context context, InputStream stream) {
        super(context);
        
        mMovie = Movie.decodeStream(stream);        
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
//        final long now = SystemClock.uptimeMillis();
//
//        if (mMoviestart == 0) { 
//            mMoviestart = now;
//        }
//
//        final int relTime = (int)((now - mMoviestart) % mMovie.duration());
//        mMovie.setTime(relTime);
//        mMovie.draw(canvas, 10, 10);
//        this.invalidate();
        
        long now=android.os.SystemClock.uptimeMillis();
        Paint p = new Paint();
        p.setAntiAlias(true);
        if (mMoviestart == 0) 
        	mMoviestart = now;
                int relTime;
                relTime = (int)((now - mMoviestart) % mMovie.duration());
                mMovie.setTime(relTime);
                mMovie.draw(canvas,0,0);
                this.invalidate();
                                 
        
    }
}