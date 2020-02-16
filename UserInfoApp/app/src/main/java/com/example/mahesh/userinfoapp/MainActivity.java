package com.example.mahesh.userinfoapp;

import android.nfc.Tag;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<DataPoint> graphPoints=new ArrayList<DataPoint>();
    DataPoint[]  obj;
    Button startBtn ;
    Button stopBtn ;
    Handler mHandler;
    GraphView graph;
    int x=0,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stopBtn = findViewById(R.id.stopbutton);
        startBtn = findViewById(R.id.startbutton);
        Button clearGraphBtn=findViewById(R.id.clearGraph);
        mHandler=new Handler();
        try {
            initGraph();
        }catch (Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        clearGraphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    clearGraph();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startPloting();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    stopPloting();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void initGraph() throws Exception{
        graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(10);
        graph.setVisibility(View.VISIBLE);
        graphPoints.add(new DataPoint(0,0));
    }
    public void startPloting() throws Exception{
//        mHandler.postDelayed(graphPlotRunnable,1000);
        plotGraphRunnable.run();
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);
    }

    public void stopPloting() throws Exception{
        mHandler.removeCallbacks(plotGraphRunnable);
        startBtn.setEnabled(true);
        stopBtn.setEnabled(false);
    }
    public void addGraphParams(int x,int y) throws Exception{
        graphPoints.add(new DataPoint(x,y));
        obj=new DataPoint[graphPoints.size()];
        graphPoints.toArray(obj);
        Log.d("Graph Points", "addGraphParams: ("+x+","+y+")");
    }
    public void clearGraph(){
        graphPoints=new ArrayList<>();
        graphPoints.add(new DataPoint(0,0));
        obj=new DataPoint[graphPoints.size()];
        graphPoints.toArray(obj);
        graph.removeAllSeries();
        x=0;
        y=0;
    }
    public Runnable plotGraphRunnable=new Runnable() {
        @Override
        public void run() {
            try {
                x++;
                int min=1;
                int max=10;
                y=(int) (Math.random()*max-min+1);
                addGraphParams(x,y);
                graph = (GraphView) findViewById(R.id.graph);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(obj);
                graph.addSeries(series);
                mHandler.postDelayed(this,2000);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                mHandler.removeCallbacks(plotGraphRunnable);
            }
        }
    };
}
