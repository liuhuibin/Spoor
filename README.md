# Spoor

Spoor is a mini android log system ,which save logs to local file.
Its memory usage is low and spoor has good performance in multi-thread.

### Download
```gradle
    dependencies {
                implementation 'com.kedacom.spoor:spoor:1.0.0'
    }
```


### Usage

```Java

//Must call Spoor#init first ,
Spoor.init(getApplicationContext());

Spoor.getInstance().i("Info log!");
Spoor.getInstance().log(LogLevel.INFO, "Info log!->by log");

Spoor.getInstance().d("Debug log!");
Spoor.getInstance().log(LogLevel.DEBUG, "Debug log!->by log");

Spoor.getInstance().w("Warning log!");
Spoor.getInstance().log(LogLevel.WARNING, "Warning log!->by log");

Spoor.getInstance().e("Error log!");
Spoor.getInstance().log(LogLevel.ERROR, "Error log!->by log");

```
