import React from "react";
import axios from "axios";

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
  
  componentDidMount = () => {
    axios.get('/data').then((response) => {
      const json = response.data;
      this.setState({
        sunrise: json.sunInfo.sunrise,
        sunset: json.sunInfo.sunset,
        temperature: json.temperature,
        noRequests: json.noRequests
      })
    })
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