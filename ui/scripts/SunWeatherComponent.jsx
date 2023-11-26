import React from "react";

class SunWeatherComponent extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      sunrise: null,
      sunset: null,
      temperature: null,
      noRequests: null
    }
  }
  render = () => {
    return <div>
      <div>Sunrise: {this.state.sunrise}</div>
      <div>Sunset: {this.state.sunset}</div>
      <div>Temperature: {this.state.temperature}</div>
      <div>Requests: {this.state.noRequests}</div>
    </div>
  }
}

export default SunWeatherComponent