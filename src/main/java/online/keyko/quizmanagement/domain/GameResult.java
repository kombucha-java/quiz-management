package online.keyko.quizmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A GameResult.
 */
@Entity
@Table(name = "game_result")
public class GameResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "place", nullable = false)
    private Integer place;

    @NotNull
    @Min(value = 0)
    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "link")
    private String link;

    @Lob
    @Column(name = "jhi_table")
    private byte[] table;

    @Column(name = "jhi_table_content_type")
    private String tableContentType;

    @JsonIgnoreProperties(value = { "gameResult", "franchise", "team", "restaurant" }, allowSetters = true)
    @OneToOne(mappedBy = "gameResult")
    private Game game;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GameResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlace() {
        return this.place;
    }

    public GameResult place(Integer place) {
        this.setPlace(place);
        return this;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Integer getPoints() {
        return this.points;
    }

    public GameResult points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getLink() {
        return this.link;
    }

    public GameResult link(String link) {
        this.setLink(link);
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public byte[] getTable() {
        return this.table;
    }

    public GameResult table(byte[] table) {
        this.setTable(table);
        return this;
    }

    public void setTable(byte[] table) {
        this.table = table;
    }

    public String getTableContentType() {
        return this.tableContentType;
    }

    public GameResult tableContentType(String tableContentType) {
        this.tableContentType = tableContentType;
        return this;
    }

    public void setTableContentType(String tableContentType) {
        this.tableContentType = tableContentType;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        if (this.game != null) {
            this.game.setGameResult(null);
        }
        if (game != null) {
            game.setGameResult(this);
        }
        this.game = game;
    }

    public GameResult game(Game game) {
        this.setGame(game);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameResult)) {
            return false;
        }
        return id != null && id.equals(((GameResult) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameResult{" +
            "id=" + getId() +
            ", place=" + getPlace() +
            ", points=" + getPoints() +
            ", link='" + getLink() + "'" +
            ", table='" + getTable() + "'" +
            ", tableContentType='" + getTableContentType() + "'" +
            "}";
    }
}
