import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGameResult } from 'app/shared/model/game-result.model';
import { getEntities as getGameResults } from 'app/entities/game-result/game-result.reducer';
import { IFranchise } from 'app/shared/model/franchise.model';
import { getEntities as getFranchises } from 'app/entities/franchise/franchise.reducer';
import { ITeam } from 'app/shared/model/team.model';
import { getEntities as getTeams } from 'app/entities/team/team.reducer';
import { IRestaurant } from 'app/shared/model/restaurant.model';
import { getEntities as getRestaurants } from 'app/entities/restaurant/restaurant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './game.reducer';
import { IGame } from 'app/shared/model/game.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GameUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const gameResults = useAppSelector(state => state.gameResult.entities);
  const franchises = useAppSelector(state => state.franchise.entities);
  const teams = useAppSelector(state => state.team.entities);
  const restaurants = useAppSelector(state => state.restaurant.entities);
  const gameEntity = useAppSelector(state => state.game.entity);
  const loading = useAppSelector(state => state.game.loading);
  const updating = useAppSelector(state => state.game.updating);
  const updateSuccess = useAppSelector(state => state.game.updateSuccess);
  const handleClose = () => {
    props.history.push('/game');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getGameResults({}));
    dispatch(getFranchises({}));
    dispatch(getTeams({}));
    dispatch(getRestaurants({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateTime = convertDateTimeToServer(values.dateTime);

    const entity = {
      ...gameEntity,
      ...values,
      gameResult: gameResults.find(it => it.id.toString() === values.gameResult.toString()),
      franchise: franchises.find(it => it.id.toString() === values.franchise.toString()),
      team: teams.find(it => it.id.toString() === values.team.toString()),
      restaurant: restaurants.find(it => it.id.toString() === values.restaurant.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateTime: displayDefaultDateTime(),
        }
      : {
          ...gameEntity,
          dateTime: convertDateTimeFromServer(gameEntity.dateTime),
          gameResult: gameEntity?.gameResult?.id,
          franchise: gameEntity?.franchise?.id,
          team: gameEntity?.team?.id,
          restaurant: gameEntity?.restaurant?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="quizManagementApp.game.home.createOrEditLabel" data-cy="GameCreateUpdateHeading">
            <Translate contentKey="quizManagementApp.game.home.createOrEditLabel">Create or edit a Game</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="game-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('quizManagementApp.game.dateTime')}
                id="game-dateTime"
                name="dateTime"
                data-cy="dateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('quizManagementApp.game.theme')}
                id="game-theme"
                name="theme"
                data-cy="theme"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="game-gameResult"
                name="gameResult"
                data-cy="gameResult"
                label={translate('quizManagementApp.game.gameResult')}
                type="select"
              >
                <option value="" key="0" />
                {gameResults
                  ? gameResults.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.place}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="game-franchise"
                name="franchise"
                data-cy="franchise"
                label={translate('quizManagementApp.game.franchise')}
                type="select"
              >
                <option value="" key="0" />
                {franchises
                  ? franchises.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.franchiseName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="game-team" name="team" data-cy="team" label={translate('quizManagementApp.game.team')} type="select">
                <option value="" key="0" />
                {teams
                  ? teams.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.teamName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="game-restaurant"
                name="restaurant"
                data-cy="restaurant"
                label={translate('quizManagementApp.game.restaurant')}
                type="select"
              >
                <option value="" key="0" />
                {restaurants
                  ? restaurants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.restaurantName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/game" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default GameUpdate;
