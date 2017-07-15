package com.example.angry.magicgestureview;



import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by angry on 15.07.17.
 */

public class MagicGestureView extends GridLayout {
    int[][] matrix;
    Context context;
    String TAG = "Magic_View";

    final Boolean LEFT_OR_UP = true;
    final Boolean RIGHT_OR_DOWN = false;

    public MagicGestureView(Context context) {
        super(context);
    }

    ArrayList<View> allViews = null;
    int rows,cols;
    float lastx,lasty;
    boolean touched;
    public float PRECISE=5.0f;
    float cx,cy;



    void init(Context context,int rows, int cols,ArrayList<View> data) {
        this.allViews = data;
        this.rows = rows;
        this.cols = cols;
        this.context = context;
        this.setRowCount(rows);
        this.setColumnCount(cols);

        matrix = new int[rows][cols];
        calculateMatrix();

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case android.view.MotionEvent.ACTION_MOVE: {
                        Log.d("xz", "moved");
                        if (!touched) return true;
                        cx = motionEvent.getX();
                        cy = motionEvent.getY();

                        if (cx - lastx > 0.001 + 1 * PRECISE) {
                            transformMatrixX(RIGHT_OR_DOWN); // MOVE RIGHT X
                        }
                        else {
                            if (Math.abs(cx - lastx) > 0.001 + 1 * PRECISE) {
                                transformMatrixX(LEFT_OR_UP); // MOVE LEFT X
                            }
                        }

                        if (cy - lasty > 0.001 + 1 * PRECISE) {
                            transformMatrixY(RIGHT_OR_DOWN);
                        }
                        else {
                            if (Math.abs(cy - lasty) > 0.001 + 1 * PRECISE) {
                                transformMatrixY(LEFT_OR_UP);
                            }
                        }

                        lastx = motionEvent.getX();
                        lasty = motionEvent.getY();
                       // calculateMatrix();
                        invalidate();
                        return true;
                    }
                   case android.view.MotionEvent.ACTION_DOWN: {
                        Log.d("xz", "down");
                        touched = true;
                        lastx = motionEvent.getX();
                        lasty = motionEvent.getY();
                        return true;
                    }
                    case android.view.MotionEvent.ACTION_UP: {
                        touched = false;
                        Log.d("xz", "up");
                        return true;
                    }
                    default: {
                        Log.d("xz", "sfsfs");
                        return true;
                    }
                }
            }
        });

        Log.d(TAG, "init: init finished");
    }


    void calculateMatrix() {
        int c = 0;
        for (int i = 0; i<rows;i++) {
            for (int j=0; j<cols; j++) {
                matrix[i][j] = c++ ;
            }
        }
    }

    private void transformMatrixY(boolean direction) {
//        System.out.println("transform y");
  //      testMatrix("BEFORE");


        if (direction==LEFT_OR_UP){
            System.out.println("UP");
            int[] temp = matrix[0];
            for (int i=1;i<rows;i++){
                matrix[i-1] = matrix[i];
            }
            matrix[rows-1] = temp;
        }
        else {
            System.out.println("DOWN");
            int[] temp = matrix[rows-1];
            for (int i=rows-1;i>0;i--){
                matrix[i] = matrix[i-1];
            }
            matrix[0] = temp;
        }

  //      testMatrix("AFTER");
    }

    private void transformMatrixX(boolean direction){
     /*   System.out.println("transform x");
        testMatrix("BEFORE");
*/
        int[] temp = new int[rows];
        if (direction==LEFT_OR_UP){
            System.out.println("LEFT");
            for (int i=0;i<rows;i++){
                temp[i]=matrix[i][0];
            }
            for (int i=0;i<rows;i++)
                for (int j=0;j<cols-1;j++){
                    matrix[i][j] = matrix[i][j+1];
                }

            for (int i=0;i<rows;i++){
                matrix[i][cols-1] = temp[i];
            }
        }
        else {
            System.out.println("RIGHT");
            for (int i=0;i<rows;i++){
                temp[i]=matrix[i][cols-1];
            }
            for (int i=0;i<rows;i++)
                for (int j=cols-1;j>0;j--){
                    matrix[i][j] = matrix[i][j-1];
                }

            for (int i=0;i<rows;i++){
                matrix[i][0] = temp[i];
            }
        }

//        testMatrix("AFTER");
    }


    private void testMatrix(String label){
        System.out.println(label);
        for (int i = 0; i<rows; i++){
            for (int j = 0; j<cols; j++) {
                System.out.print(matrix[i][j]);
            }
            System.out.println("");
        }
    }


    ArrayList<LinearLayout> flist;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int twidth = this.getWidth();
        int theight = this.getHeight();

        TableLayout.LayoutParams lpw = new TableLayout.LayoutParams(twidth / cols, theight / rows, 2);

        if (flist != null) {
            for (LinearLayout f : flist) {
                f.removeAllViews();
            }
        }

        flist = new ArrayList<>(rows * cols);
        this.removeAllViews();
        flist.clear();
        for (int i = 0; i < rows * cols; i++) {
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            flist.add(ll);
        }
        //   Log.d(TAG, "onDraw: main" + this.getChildCount());

        int c = 0;
        int cPos = 0, rPos = 0;

        for (int i = 0; i < rows; i++) {
            cPos=0;
            for (int j = 0; j < cols; j++) {
                LinearLayout fl = flist.get(c);
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                lp.columnSpec = GridLayout.spec(cPos++ % cols, 1.0f);
                lp.rowSpec = GridLayout.spec(rPos % rows, 1.0f);

                fl.setLayoutParams(lp);
                //    Log.d(TAG, "onDraw: fl " + c + " " + fl);
                fl.removeAllViews();
                //  allViews.get(c).setBackgroundColor((c % 2 == 0 )? Color.GREEN : Color.WHITE );
                //    Log.d(TAG, "onDraw: " + fl.getChildCount());
                View v = allViews.get(matrix[i][j]);
                v.setLayoutParams(lp);
                v.setBackgroundColor((c++ % 2 == 0) ? Color.GREEN : Color.WHITE);
                //fl.addView(v);
              //  fl.setBackgroundColor(Color.WHITE);
                this.addView(v);
            }
            rPos++;
        }
    }
}
