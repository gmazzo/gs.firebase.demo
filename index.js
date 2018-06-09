import {AppRegistry} from 'react-native';
import firebase from 'firebase';
import App from './App';

// Initialize Firebase
const firebaseConfig = {
    apiKey: "AIzaSyCXPEmib6F1Xjg-yZGQDYQuPi0mZRQ0NRs",
    authDomain: "fir-react-native-demo.firebaseapp.com",
    databaseURL: "https://fir-react-native-demo.firebaseio.com",
    projectId: "fir-react-native-demo",
    storageBucket: "fir-react-native-demo.appspot.com",
    messagingSenderId: "221100277552"
};
firebase.initializeApp(firebaseConfig);

AppRegistry.registerComponent('app', () => App);
