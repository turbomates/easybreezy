import React, { FC, useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { Layout, Drawer, Button } from "antd";
import { MenuOutlined } from "@ant-design/icons";
import { SiderMenu } from "features/app/components/SiderMenu";
import { HeaderProfileDropdown } from "features/app/components/HeaderProfileDropdown";
import { account } from "features/account/selectors";
import { fetchProfileAsync } from "features/human-resouce/actions";

import "./Main.scss";

const { Header, Sider, Content } = Layout;

type Props = {};

export const Main: FC<Props> = ({ children }) => {
  const [collapsed, setCollapsed] = useState(false);
  // TODO check relogin bugs
  const { profile, loading } = useSelector(account);
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchProfileAsync.request());
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const [menuVisible, setMenuVisible] = useState(false);

  const showDrawer = () => setMenuVisible(true);
  const onClose = () => setMenuVisible(false);

  return (
    <Layout className="app-layout">
      <Drawer
        className="app-mobile-drawer"
        placement="left"
        closable={false}
        onClose={onClose}
        visible={menuVisible}
      >
        <SiderMenu />
      </Drawer>
      <Sider
        width={200}
        className="app-sider"
        collapsible
        collapsed={collapsed}
        onCollapse={setCollapsed}
      >
        <SiderMenu />
      </Sider>
      <Layout className="">
        <Header className="app-header">
          <Button
            className="app-mobile-drawer-toggle"
            type="primary"
            onClick={showDrawer}
          >
            <MenuOutlined />
          </Button>
          <Link to="/">Easybreezy</Link>
          <HeaderProfileDropdown profile={profile} loading={loading} />
        </Header>
        <Content>{children}</Content>
        {/* <Footer className="footer">
          Turbomates Soft {new Date().getFullYear()}
        </Footer> */}
      </Layout>
    </Layout>
  );
};
