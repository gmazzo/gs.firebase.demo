import {StackNavigator} from 'react-navigation';
import LoginScreen from "./components/screens/LoginScreen";

const App = StackNavigator({
    Login: {screen: LoginScreen},
});

export default App;
