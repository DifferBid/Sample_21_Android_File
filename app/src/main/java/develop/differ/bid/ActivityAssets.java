package develop.differ.bid;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ActivityAssets extends AppCompatActivity {


    /*
    Создание папки assets:
    В режиме Project View: Кликните правой кнопкой мыши на папке main.
    Затем выберите New -> Folder -> Assets Folder. Следуйте инструкциям мастера для создания папки.

    В режиме Android View: Этот режим автоматически группирует все исходные файлы,
    ресурсы и элементы конфигурации. Кликните правой кнопкой мыши на папке app и выберите New -> Folder -> Assets Folder.

    Чтобы создать подпапку внутри assets, кликните правой кнопкой мыши на папке assets,
    выберите New -> Directory и введите название новой подпапки. Например,
    если вы хотите создать подпапку для аудиофайлов, назовите её audio.

    Пример структуры папок в assets:
        assets/
    ├── images/
    │   ├── logo.png
    │   └── background.jpg
    ├── audio/
    │   ├── background_music.mp3
    │   └── click_sound.mp4
    └── data/
        ├── config.json
        └── sample_data.xml
     */

    private ImageView imageView;
    private final Handler handler = new Handler();
    private String[] imageFiles;
    private int currentIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asseets);
        // Для доступа к ресурсам в папке assets используем AssetManager через контекст приложения
        Context context = this;
        AssetManager assetManager = context.getAssets();
        // Получить список файлов в папке или список папок:
        try {
            String[] folder = assetManager.list("");
            Log.d("MyAppLog", "folder: " + Arrays.toString(folder));

            String[] files = assetManager.list("catalog_one");
            Log.d("MyAppLog", "files: " + Arrays.toString(files));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        InputStream inputStream;
        try {
            inputStream = assetManager.open("image/image_02.png");
            Log.d("MyAppLog", "inputStream: " + inputStream);

            InputStream is = this.getAssets().open("image/image_01.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        // Устанавливаем изображение в ImageView
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);

        try {
            // Закрываем inputStream чтобы освободить системные ресурсы, которые он занимает
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // try-with-resources конструкция автоматически закрывает ресурсы после использования
        try (InputStream inputStreamOne = assetManager.open("image/image_01.png")) {
            Bitmap bitmapOne = BitmapFactory.decodeStream(inputStreamOne);
            // Используйте bitmap здесь
        } catch (IOException e) {
            // Обработка исключения
        }

        // Цикл с задержкой в одну секунду, который будет перебирать массив файлов
        // и устанавливать изображения в ImageView
        // searchOfPictures();
    }

    private void searchOfPictures(){

        imageView = findViewById(R.id.imageView);

        try {
            // Предполагаем, что все изображения находятся в папке assets/images
            imageFiles = getAssets().list("image");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Запускаем цикл с задержкой
        runnable.run();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (imageFiles != null && currentIndex < imageFiles.length) {
                AssetManager assetManager = getAssets();
                try (InputStream inputStream = assetManager.open("image/" + imageFiles[currentIndex])) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                currentIndex++;
            } else {
                currentIndex = 0; // Сбросить индекс, если достигли конца массива
            }
            handler.postDelayed(this, 1000); // Повторяем каждую секунду
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Удаляем callbacks при уничтожении Activity
        handler.removeCallbacks(runnable);
    }

}
