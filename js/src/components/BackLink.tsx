import React from "react";
import areEqual from "fast-deep-equal";

import { Link } from "react-router-dom";

interface Props {}

export const BackLink = React.memo<Props>(() => {
  return (
    <Link to="/" className="link">
      {"< Back"}
    </Link>
  );
}, areEqual);
