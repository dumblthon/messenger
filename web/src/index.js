import React from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import './index.css';
import {Comment, Tooltip, List} from 'antd';
import moment from 'moment';


class ChatComponent extends React.Component {
    intervalID;

    state = {
        data: [],
    }

    componentDidMount() {
        this.getData();
    }

    componentWillUnmount() {
        /*
          stop getData() from continuing to run even
          after unmounting this component. Notice we are calling
          'clearTimeout()` here rather than `clearInterval()` as
          in the previous example.
        */
        clearTimeout(this.intervalID);
    }


    getData = () => {

        console.log(123)
        fetch('http://localhost:3000/messages')
            .then(response => response.json())
            .then(newData => {
                this.setState((prevState, props) => ({data: newData}));
                this.intervalID = setTimeout(this.getData.bind(this), 5000);
            });
    }

    render() {
        return (
            <List
                className="comment-list"
                header={`Chat`}
                itemLayout="horizontal"
                dataSource={this.state.data}
                renderItem={item => (
                    <li>
                        <Comment
                            actions={[<span key="comment-list-reply-to-0">Reply to</span>]}
                            author={item.author}
                            avatar={item.avatar}
                            content={item.content}
                            datetime={
                                <Tooltip
                                title={moment()
                                    .subtract(2, 'days')
                                    .format('YYYY-MM-DD HH:mm:ss')}>
                                    <span>
                                      {moment()
                                          .subtract(2, 'days')
                                          .fromNow()}
                                    </span>
                                </Tooltip>}
                        />
                    </li>
                )}
            />
        );
    }
}

ReactDOM.render(<ChatComponent/>, document.getElementById('container'));