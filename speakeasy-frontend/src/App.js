import logo from './logo.svg';
import './App.css';
import React, {Component} from 'react';

class App extends Component<{}, {}> {

    constructor() {
        super();
    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={
                        logo
                    }
                         className="App-logo" alt="logo"/>
                    <p>
                        Edit <code>src/App.js</code> and save to reload.
                    </p>
                    <textarea
                        rows={15}
                        cols={50}
                        />
                    <a
                        className="App-link"
                        href="https://reactjs.org"
                        target="_blank"
                        rel="noopener noreferrer"
                    >
                        Learn React
                    </a>
                </header>
            </div>
        );
    }
}

export default App;
