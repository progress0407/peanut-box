package philo.peanutbox.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

public class Hello {

  @Test
  void asd() {
    assertAll(
        () -> assertThat(1).isNotNull()
    );
  }

}
