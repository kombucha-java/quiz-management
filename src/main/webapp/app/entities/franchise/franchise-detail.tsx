import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './franchise.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FranchiseDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const franchiseEntity = useAppSelector(state => state.franchise.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="franchiseDetailsHeading">
          <Translate contentKey="quizManagementApp.franchise.detail.title">Franchise</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{franchiseEntity.id}</dd>
          <dt>
            <span id="franchiseName">
              <Translate contentKey="quizManagementApp.franchise.franchiseName">Franchise Name</Translate>
            </span>
          </dt>
          <dd>{franchiseEntity.franchiseName}</dd>
          <dt>
            <Translate contentKey="quizManagementApp.franchise.gameType">Game Type</Translate>
          </dt>
          <dd>{franchiseEntity.gameType ? franchiseEntity.gameType.gameTypeName : ''}</dd>
        </dl>
        <Button tag={Link} to="/franchise" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/franchise/${franchiseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FranchiseDetail;
