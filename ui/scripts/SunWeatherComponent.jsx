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
    return <table><tbody>
      <tr>
        <td>Sunrise:</td>
        <td>{this.state.sunrise}</td>
      </tr>
      <tr>
        <td>Sunset:</td>
        <td>{this.state.sunset}</td>
      </tr>
      <tr>
        <td>Temperature:</td>
        <td>{this.state.temperature}</td>
      </tr>
      <tr>
        <td>Requests:</td>
        <td>{this.state.noRequests}</td>
      </tr>
    </tbody></table>
  }
}

export default SunWeatherComponent