import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGameType } from 'app/shared/model/game-type.model';
import { getEntities as getGameTypes } from 'app/entities/game-type/game-type.reducer';
import { getEntity, updateEntity, createEntity, reset } from './franchise.reducer';
import { IFranchise } from 'app/shared/model/franchise.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FranchiseUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const gameTypes = useAppSelector(state => state.gameType.entities);
  const franchiseEntity = useAppSelector(state => state.franchise.entity);
  const loading = useAppSelector(state => state.franchise.loading);
  const updating = useAppSelector(state => state.franchise.updating);
  const updateSuccess = useAppSelector(state => state.franchise.updateSuccess);
  const handleClose = () => {
    props.history.push('/franchise');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getGameTypes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...franchiseEntity,
      ...values,
      gameType: gameTypes.find(it => it.id.toString() === values.gameType.toString()),
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
          ...franchiseEntity,
          gameType: franchiseEntity?.gameType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="quizManagementApp.franchise.home.createOrEditLabel" data-cy="FranchiseCreateUpdateHeading">
            <Translate contentKey="quizManagementApp.franchise.home.createOrEditLabel">Create or edit a Franchise</Translate>
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
                  id="franchise-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('quizManagementApp.franchise.franchiseName')}
                id="franchise-franchiseName"
                name="franchiseName"
                data-cy="franchiseName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 80, message: translate('entity.validation.maxlength', { max: 80 }) },
                }}
              />
              <ValidatedField
                id="franchise-gameType"
                name="gameType"
                data-cy="gameType"
                label={translate('quizManagementApp.franchise.gameType')}
                type="select"
              >
                <option value="" key="0" />
                {gameTypes
                  ? gameTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.gameTypeName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/franchise" replace color="info">
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

export default FranchiseUpdate;
