import React, { FC, useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { Layout } from "antd";
import { SiderMenu } from "../features/app/components/SiderMenu";
import { HeaderProfileDropdown } from "../features/app/components/HeaderProfileDropdown";
import { account } from "features/account/selectors";
import { fetchProfileAsync } from "features/human-resouce/actions";

import logo from "../assets/logo.svg";
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

  return (
    <Layout className="app-layout">
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
        <Header className="main-header">
          <img src={logo} className="app-logo" alt="logo" />
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
