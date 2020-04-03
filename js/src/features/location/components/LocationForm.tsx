import React, { useState } from "react";

interface Props {
  create: (value: string) => void;
}

export const LocationForm: React.FC<Props> = props => {
  const [value, setValue] = useState("");

  const handleSubmit = () => props.create(value);

  return (
    <>
      <input
        type="text"
        onChange={e => setValue(e.target.value)}
        value={value}
      />
      <button onClick={handleSubmit}>Create</button>
    </>
  );
};
