import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './game-result.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GameResultDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const gameResultEntity = useAppSelector(state => state.gameResult.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gameResultDetailsHeading">
          <Translate contentKey="quizManagementApp.gameResult.detail.title">GameResult</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gameResultEntity.id}</dd>
          <dt>
            <span id="place">
              <Translate contentKey="quizManagementApp.gameResult.place">Place</Translate>
            </span>
          </dt>
          <dd>{gameResultEntity.place}</dd>
          <dt>
            <span id="points">
              <Translate contentKey="quizManagementApp.gameResult.points">Points</Translate>
            </span>
          </dt>
          <dd>{gameResultEntity.points}</dd>
          <dt>
            <span id="link">
              <Translate contentKey="quizManagementApp.gameResult.link">Link</Translate>
            </span>
          </dt>
          <dd>{gameResultEntity.link}</dd>
          <dt>
            <span id="table">
              <Translate contentKey="quizManagementApp.gameResult.table">Table</Translate>
            </span>
          </dt>
          <dd>
            {gameResultEntity.table ? (
              <div>
                {gameResultEntity.tableContentType ? (
                  <a onClick={openFile(gameResultEntity.tableContentType, gameResultEntity.table)}>
                    <img src={`data:${gameResultEntity.tableContentType};base64,${gameResultEntity.table}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {gameResultEntity.tableContentType}, {byteSize(gameResultEntity.table)}
                </span>
              </div>
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/game-result" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/game-result/${gameResultEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GameResultDetail;
