package cl.ucn.disc.dam.discnews;

import android.app.Application;

import com.facebook.device.yearclass.YearClass;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.apache.commons.lang3.time.StopWatch;

import cl.ucn.disc.dam.discnews.dao.AppDatabase;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by RaosF on 27-12-2017.
 */

@Slf4j
public final class App extends Application {

    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Timer
        final StopWatch stopWatch = StopWatch.createStarted();

        // Facebook Fresco
        {
            // Pipeline config
            final ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
                    .setDownsampleEnabled(true)
                    .setResizeAndRotateEnabledForNetwork(true)
                    .build();

            // Initialize Fresco
            Fresco.initialize(this, imagePipelineConfig);
        }

        // DBFLow
        {
            // Initialize DBFLow
            FlowManager.init(FlowConfig.builder(this)
                    .addDatabaseConfig(DatabaseConfig.builder(AppDatabase.class).databaseName("articlestore").build())
                    // .openDatabasesOnInit(true)
                    .build()
            );
        }

        // Device-Year
        {
            int year = YearClass.get(this);
            log.debug("Year detected: {}", year);
        }

        // Timming
        log.debug("Initialization in: {}", stopWatch);

    }

}

