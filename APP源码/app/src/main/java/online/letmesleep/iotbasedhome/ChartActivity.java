package online.letmesleep.iotbasedhome;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import online.letmesleep.iotbasedhome.bean.ApplicationEntity;
import online.letmesleep.iotbasedhome.bean.ApplicationEntityWithData;
import online.letmesleep.iotbasedhome.config.Config;
import online.letmesleep.iotbasedhome.util.ApplicationControlUtil;
import online.letmesleep.iotbasedhome.util.MChartUtil;

public class ChartActivity extends AppCompatActivity {

    @BindView(R.id.chart)
    LineChart chart;
    @BindView(R.id.chart_activity_charttitle)
    TextView chartActivityCharttitle;
    private List<Entry> points = new ArrayList<>();
    private Thread thread;
    private ApplicationEntityWithData aewd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        int applicationid = getIntent().getExtras().getInt("id", -1);
        initData(applicationid);
    }

    private void initData(int id) {
        if (id == -1)
            finish();
        ApplicationEntity ae = DataSupport.find(ApplicationEntity.class, id);
        if (ae == null)
            finish();
        aewd = new ApplicationEntityWithData(ae, points);
        if (aewd.getApplicationType() == Config.APPLICATION_TYPE_HUMIDITY) {
            aewd.setLimithigh(String.valueOf(Config.HIGH_HUMIDITY_STANDARD));
            aewd.setLimitlow(String.valueOf(Config.LOW_HUMIDITY_STANDARD));
        } else {
            aewd.setLimithigh(String.valueOf(Config.HIGH_TEMPERATURE));
            aewd.setLimitlow(String.valueOf(Config.LOW_TEMPERATURE));
        }
        chartActivityCharttitle.setText(Config.getApplicationType(aewd.getApplicationType()));
        thread = new Thread(updataThread);
        thread.start();
    }

    private void loadData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MChartUtil.getSettedChartWithoutLimitLine(chart, points, aewd);


            }
        });

    }

    private boolean alive = true;
    private boolean printErrOnce = true;
    Runnable updataThread = new Runnable() {
        @Override
        public void run() {
            while (alive) {
                ApplicationControlUtil.getTempAndHumidity(new ApplicationControlUtil.onTemperatureHumidityCallback() {
                    @Override
                    public void onTemperatureSuccess(float temperature) {
                        printErrOnce = true;
                        if (aewd.getApplicationType() == Config.APPLICATION_TYPE_TEMPERATURE) {
                            points.add(new Entry(points.size(), temperature));
                            loadData();
                        }
                    }

                    @Override
                    public void onHumiditySuccess(float humidity) {
                        if (aewd.getApplicationType() == Config.APPLICATION_TYPE_HUMIDITY) {
                            points.add(new Entry(points.size(), humidity));
                            loadData();
                        }
                    }

                    @Override
                    public void onFaild(final String err) {
                        if (printErrOnce) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    printErrOnce = false;
                                    Toast.makeText(ChartActivity.this, "数据格式有误：" + err, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @OnClick(R.id.chart_activity_finish)
    public void onViewClicked() {
        finish();
    }
}
