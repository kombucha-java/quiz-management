import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './game-result.reducer';
import { IGameResult } from 'app/shared/model/game-result.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GameResult = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const gameResultList = useAppSelector(state => state.gameResult.entities);
  const loading = useAppSelector(state => state.gameResult.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="game-result-heading" data-cy="GameResultHeading">
        <Translate contentKey="quizManagementApp.gameResult.home.title">Game Results</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="quizManagementApp.gameResult.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="quizManagementApp.gameResult.home.createLabel">Create new Game Result</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {gameResultList && gameResultList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="quizManagementApp.gameResult.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="quizManagementApp.gameResult.place">Place</Translate>
                </th>
                <th>
                  <Translate contentKey="quizManagementApp.gameResult.points">Points</Translate>
                </th>
                <th>
                  <Translate contentKey="quizManagementApp.gameResult.link">Link</Translate>
                </th>
                <th>
                  <Translate contentKey="quizManagementApp.gameResult.table">Table</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {gameResultList.map((gameResult, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${gameResult.id}`} color="link" size="sm">
                      {gameResult.id}
                    </Button>
                  </td>
                  <td>{gameResult.place}</td>
                  <td>{gameResult.points}</td>
                  <td>{gameResult.link}</td>
                  <td>
                    {gameResult.table ? (
                      <div>
                        {gameResult.tableContentType ? (
                          <a onClick={openFile(gameResult.tableContentType, gameResult.table)}>
                            <img src={`data:${gameResult.tableContentType};base64,${gameResult.table}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {gameResult.tableContentType}, {byteSize(gameResult.table)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${gameResult.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${gameResult.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${gameResult.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="quizManagementApp.gameResult.home.notFound">No Game Results found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default GameResult;
