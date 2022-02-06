package online.keyko.quizmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A GameType.
 */
@Entity
@Table(name = "game_type")
public class GameType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "game_type_name", length = 30, nullable = false, unique = true)
    private String gameTypeName;

    @OneToMany(mappedBy = "gameType")
    @JsonIgnoreProperties(value = { "games", "gameType" }, allowSetters = true)
    private Set<Franchise> franchises = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GameType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameTypeName() {
        return this.gameTypeName;
    }

    public GameType gameTypeName(String gameTypeName) {
        this.setGameTypeName(gameTypeName);
        return this;
    }

    public void setGameTypeName(String gameTypeName) {
        this.gameTypeName = gameTypeName;
    }

    public Set<Franchise> getFranchises() {
        return this.franchises;
    }

    public void setFranchises(Set<Franchise> franchises) {
        if (this.franchises != null) {
            this.franchises.forEach(i -> i.setGameType(null));
        }
        if (franchises != null) {
            franchises.forEach(i -> i.setGameType(this));
        }
        this.franchises = franchises;
    }

    public GameType franchises(Set<Franchise> franchises) {
        this.setFranchises(franchises);
        return this;
    }

    public GameType addFranchise(Franchise franchise) {
        this.franchises.add(franchise);
        franchise.setGameType(this);
        return this;
    }

    public GameType removeFranchise(Franchise franchise) {
        this.franchises.remove(franchise);
        franchise.setGameType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameType)) {
            return false;
        }
        return id != null && id.equals(((GameType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameType{" +
            "id=" + getId() +
            ", gameTypeName='" + getGameTypeName() + "'" +
            "}";
    }
}
