import React from 'react';
import speakeasy from './speakeasy.png';
import './App.css';
import {Component} from 'react';


class App extends Component {
    constructor() {
        super();
        this.state = {
            name: "",
            message: "",
            showMenu: false,
            voices: null
        };
        this.getVoice();
        this.showMenu = this.showMenu.bind(this);
        this.closeMenu = this.closeMenu.bind(this);
    }

    showMenu(event) {
        event.preventDefault();

        this.setState({showMenu: true}, () => {
            document.addEventListener('click', this.closeMenu);
        });
    }

    getVoice = async () => {
        try {
            const response = fetch("/voices");
            let data = await response;

            if (!data.ok) {
                alert("There was an issue loading the app");
                return;
            }

            let voicesPromise = data.json();
            let voicesData = await voicesPromise;
            this.setState({
                voices: voicesData
            })

        } catch {
            alert("unable to contact server")
        }
    }

    speak = async () => {
        let state = {
            Name: this.state.name,
            message: this.state.message
        }
        try {
            const response = await fetch("/speak", JSON.stringify(state));
            const data = await response.json()
            this.setState({
                message: ""
            })

        } catch {
            alert("unable to contact server")

        }
    }

    closeMenu() {
        this.setState({showMenu: false}, () => {
            document.removeEventListener('click', this.closeMenu);
        });
    }

    handleNameChange = (givenName) => {
        this.setState({
            name: givenName
        })
    }
    handleMessageChange = (given) => {
        this.setState({
            message: given
        })
    }

    onClickSubmit = () => {
        this.speak();
    }


    render() {
        return (
            <div className="App">

                <img src={speakeasy} className="App-logo" alt="logo"/>
                <h1>SpeakEasy
                </h1>
                <label className="name">
                    Name:
                    <br/>
                    <input type="text" value={this.state.Name} onChange={(event) => {
                        this.handleNameChange(event.target.value)
                    }}/>
                </label>
                <br/>
                Message:
                <textarea
                    row={15}
                    col={50}
                    onChange={(event) => {
                        this.handleMessageChange(event.target.value)
                    }}
                    value={this.state.message}
                />
                <br/>
                <button
                    onClick={() => {
                        this.onClickSubmit()
                    }}
                >
                    Submit
                </button>

                <button onClick={this.showMenu} className="showbtn">
                    Show menu
                </button>

                {
                    this.state.showMenu
                        ? (
                            <div className="menu">
                                <button> Menu item 1</button>
                                <button> Menu item 2</button>
                                <button> Menu item 3</button>
                                <button>Menu item 4</button>
                                <button>Menu item 5</button>
                                <button>Menu item 6</button>
                                <button>Menu item 7</button>
                                <button>Menu item 8</button>
                                <button>Menu item 9</button>
                                <button>Menu item 10</button>
                            </div>
                        )
                        : (
                            null
                        )
                }

            </div>
        );
    }
}

export default App;
