package openag.boot.quartz;

import java.util.concurrent.Executor;

/**
 * Extension to supply executor implementation that will run quartz jobs
 * <p/>
 *
 * @author Andrei Maus
 */
public interface ExecutorFactory {

  /**
   * @return {@link Executor} instance for quartz jobs execution
   */
  Executor getExecutor();

}
