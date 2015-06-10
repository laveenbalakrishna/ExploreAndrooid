package com.example.laveen.exploreandrooid;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class SurfaceActivity extends ActionBarActivity {

    private GLSurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new OpenGlView(this);
        setContentView(view);
    }

    static class OpenGlView extends GLSurfaceView {

        ViewRenderer renderer;

        public OpenGlView(Context context) {
            super(context);
            renderer = new ViewRenderer();
            setRenderer(renderer);
            setRenderMode(RENDERMODE_WHEN_DIRTY);
        }

        @Override
        public boolean onTouchEvent(final MotionEvent event) {
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    renderer.setColor(event.getX() / getWidth(), event.getY() / getHeight(), 1.0f);
                }
            });
            requestRender();
            return true;
        }
    }

    static class ViewRenderer implements GLSurfaceView.Renderer {

        private float r;
        private float g;
        private float b;

        public ViewRenderer(){
            r=0;
            g=1;
            b=1;
        }

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int i, int i2) {
            gl10.glViewport(0, 0, i, i2);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            Log.d("RENDER", "onDrawFrame");
            gl10.glClearColor(r, g, b, 1.0f);
            gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        }

        public void setColor(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }


}
