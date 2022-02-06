import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './game.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GameDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const gameEntity = useAppSelector(state => state.game.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gameDetailsHeading">
          <Translate contentKey="quizManagementApp.game.detail.title">Game</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gameEntity.id}</dd>
          <dt>
            <span id="dateTime">
              <Translate contentKey="quizManagementApp.game.dateTime">Date Time</Translate>
            </span>
          </dt>
          <dd>{gameEntity.dateTime ? <TextFormat value={gameEntity.dateTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="theme">
              <Translate contentKey="quizManagementApp.game.theme">Theme</Translate>
            </span>
          </dt>
          <dd>{gameEntity.theme}</dd>
          <dt>
            <Translate contentKey="quizManagementApp.game.gameResult">Game Result</Translate>
          </dt>
          <dd>{gameEntity.gameResult ? gameEntity.gameResult.place : ''}</dd>
          <dt>
            <Translate contentKey="quizManagementApp.game.franchise">Franchise</Translate>
          </dt>
          <dd>{gameEntity.franchise ? gameEntity.franchise.franchiseName : ''}</dd>
          <dt>
            <Translate contentKey="quizManagementApp.game.team">Team</Translate>
          </dt>
          <dd>{gameEntity.team ? gameEntity.team.teamName : ''}</dd>
          <dt>
            <Translate contentKey="quizManagementApp.game.restaurant">Restaurant</Translate>
          </dt>
          <dd>{gameEntity.restaurant ? gameEntity.restaurant.restaurantName : ''}</dd>
        </dl>
        <Button tag={Link} to="/game" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/game/${gameEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GameDetail;
