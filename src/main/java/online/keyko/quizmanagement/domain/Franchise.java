package online.keyko.quizmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Franchise.
 */
@Entity
@Table(name = "franchise")
public class Franchise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "franchise_name", length = 80, nullable = false, unique = true)
    private String franchiseName;

    @OneToMany(mappedBy = "franchise")
    @JsonIgnoreProperties(value = { "gameResult", "franchise", "team", "restaurant" }, allowSetters = true)
    private Set<Game> games = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "franchises" }, allowSetters = true)
    private GameType gameType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Franchise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFranchiseName() {
        return this.franchiseName;
    }

    public Franchise franchiseName(String franchiseName) {
        this.setFranchiseName(franchiseName);
        return this;
    }

    public void setFranchiseName(String franchiseName) {
        this.franchiseName = franchiseName;
    }

    public Set<Game> getGames() {
        return this.games;
    }

    public void setGames(Set<Game> games) {
        if (this.games != null) {
            this.games.forEach(i -> i.setFranchise(null));
        }
        if (games != null) {
            games.forEach(i -> i.setFranchise(this));
        }
        this.games = games;
    }

    public Franchise games(Set<Game> games) {
        this.setGames(games);
        return this;
    }

    public Franchise addGame(Game game) {
        this.games.add(game);
        game.setFranchise(this);
        return this;
    }

    public Franchise removeGame(Game game) {
        this.games.remove(game);
        game.setFranchise(null);
        return this;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public Franchise gameType(GameType gameType) {
        this.setGameType(gameType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Franchise)) {
            return false;
        }
        return id != null && id.equals(((Franchise) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Franchise{" +
            "id=" + getId() +
            ", franchiseName='" + getFranchiseName() + "'" +
            "}";
    }
}
