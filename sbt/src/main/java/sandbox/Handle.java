package sandbox;

import java.util.concurrent.Future;

public interface Handle<T> extends Future<T> {

  void addListener(Listener<T> l);

  static interface Listener<T> {

    void success(Handle<T> handle, T output);
    void error(Handle<T> handle, Throwable t);
  }
}
