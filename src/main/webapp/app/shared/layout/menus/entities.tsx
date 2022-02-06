import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/franchise">
      <Translate contentKey="global.menu.entities.franchise" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/game-type">
      <Translate contentKey="global.menu.entities.gameType" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/team">
      <Translate contentKey="global.menu.entities.team" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/player">
      <Translate contentKey="global.menu.entities.player" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/game-result">
      <Translate contentKey="global.menu.entities.gameResult" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/restaurant">
      <Translate contentKey="global.menu.entities.restaurant" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/game">
      <Translate contentKey="global.menu.entities.game" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
