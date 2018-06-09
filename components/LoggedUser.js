import React, {Component} from 'react';
import firebase from 'firebase';
import {Text} from "react-native";

export default class LoggedUser extends Component {

    constructor(props) {
        super(props);

        this.state = {
            user: null
        }
    }

    render() {
        return (
            <Text>{this.state.user ? this.state.user.name : 'Firebase React Native Demo'}</Text>
        );
    }

    componentDidMount() {
        this.unsubscribe = firebase.auth().onAuthStateChanged((user) => {
            this.setState({
                user: user
            })
        });
    }

    componentWillUnmount() {
        this.unsubscribe();
    }

}
