package online.keyko.quizmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

import online.keyko.quizmanagement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameType.class);
        GameType gameType1 = new GameType();
        gameType1.setId(1L);
        GameType gameType2 = new GameType();
        gameType2.setId(gameType1.getId());
        assertThat(gameType1).isEqualTo(gameType2);
        gameType2.setId(2L);
        assertThat(gameType1).isNotEqualTo(gameType2);
        gameType1.setId(null);
        assertThat(gameType1).isNotEqualTo(gameType2);
    }
}
