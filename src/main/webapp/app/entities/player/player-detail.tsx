import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './player.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PlayerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const playerEntity = useAppSelector(state => state.player.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="playerDetailsHeading">
          <Translate contentKey="quizManagementApp.player.detail.title">Player</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{playerEntity.id}</dd>
          <dt>
            <span id="nickName">
              <Translate contentKey="quizManagementApp.player.nickName">Nick Name</Translate>
            </span>
          </dt>
          <dd>{playerEntity.nickName}</dd>
          <dt>
            <Translate contentKey="quizManagementApp.player.internalUser">Internal User</Translate>
          </dt>
          <dd>{playerEntity.internalUser ? playerEntity.internalUser.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/player" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/player/${playerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PlayerDetail;
