import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGame } from 'app/shared/model/game.model';
import { getEntities as getGames } from 'app/entities/game/game.reducer';
import { getEntity, updateEntity, createEntity, reset } from './game-result.reducer';
import { IGameResult } from 'app/shared/model/game-result.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GameResultUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const games = useAppSelector(state => state.game.entities);
  const gameResultEntity = useAppSelector(state => state.gameResult.entity);
  const loading = useAppSelector(state => state.gameResult.loading);
  const updating = useAppSelector(state => state.gameResult.updating);
  const updateSuccess = useAppSelector(state => state.gameResult.updateSuccess);
  const handleClose = () => {
    props.history.push('/game-result');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getGames({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...gameResultEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...gameResultEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="quizManagementApp.gameResult.home.createOrEditLabel" data-cy="GameResultCreateUpdateHeading">
            <Translate contentKey="quizManagementApp.gameResult.home.createOrEditLabel">Create or edit a GameResult</Translate>
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
                  id="game-result-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('quizManagementApp.gameResult.place')}
                id="game-result-place"
                name="place"
                data-cy="place"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('quizManagementApp.gameResult.points')}
                id="game-result-points"
                name="points"
                data-cy="points"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('quizManagementApp.gameResult.link')}
                id="game-result-link"
                name="link"
                data-cy="link"
                type="text"
              />
              <ValidatedBlobField
                label={translate('quizManagementApp.gameResult.table')}
                id="game-result-table"
                name="table"
                data-cy="table"
                isImage
                accept="image/*"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/game-result" replace color="info">
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

export default GameResultUpdate;
