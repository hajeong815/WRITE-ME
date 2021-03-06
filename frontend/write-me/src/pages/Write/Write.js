import React, { useState, useEffect } from 'react';
import { useLocation } from "react-router-dom";
import moment from "moment";
import "./Write.css";
import axios from "axios";
import qs from 'querystring'
import {history} from "../../helpers";
import {useSelector} from "react-redux";

function Write() {
    const location = useLocation();
    const { keywordName, keywordID, categoryID } = location.state;
    const user = useSelector(state => state.authentication);
    const [state, setState] = useState({
        loading: true,
        error: null,
        hint: null
    });

    useEffect(() => {
        axios.get(`https://readme-writeme.appspot.com/api/posts/hint?keywordID=${keywordID}`)
            .then(data => {
                setState({
                    ...state,
                    loading: false,
                    hint: data.data
                });
            })
            .catch(error => {
                setState({ ...state, loading: false, error });
            });
        console.log(state.hint);
    }, []);

    const [post, setPost] = useState({
        postTitle: '',
        postDetail: '',
    });

    function handleChange(e) {
        const { name, value } = e.target;
        setPost(post => ({ ...post, [name]: value }));
    }

    function handleSubmit(e) {
        e.preventDefault();
        const date = moment().format();
        if (post.postTitle === "") {
            return alert('제목을 입력해주세요');
        } else if (post.postDetail === "") {
            return alert('내용을 입력해주세요.');
        }

        if (post.postTitle && post.postDetail) {
            axios({
                method: 'post',
                url: `https://readme-writeme.appspot.com/api/post`,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/x-www-form-urlencoded;'
                },
                data: qs.stringify({
                    postTitle: post.postTitle,
                    postDetail: post.postDetail,
                    username: user.username,
                    keywordID,
                    categoryID,
                    date
                })
            }).then(data => {
                    const res = data.data;
                    console.log(res);
                    history.push(`post/@${res.username}/${res.postID}`);
                })
                .catch(error => {
                    alert("글 작성 실패");
                });
        }
    }

    return (
        <div className='Mains'>
          <div id='Mains-left'/>
          <div className='Write'>
              <form className='Write' onSubmit={handleSubmit}>
                <div>
                  <input type='text' id='title_txt' name='postTitle' onChange={handleChange} placeholder='제목'/>
                </div>
                <div>
                    <p id='keyword_txt1'>
                    <span className="keywordName"> {keywordName}:  </span>
                    {state.loading ? (
                        <span className="spinner-border spinner-border-sm mr-1"/>
                    ) : (
                        <>
                            {state.hint === undefined || state.hint === null ?
                                <span> 해당되는 힌트가 없습니다. </span>
                                :
                                <>
                                    {state.hint.map(hint => (
                                        <span className="hint" key={hint.hintID}> {hint.hintName} </span>
                                    ))}
                                </>
                            }
                        </>
                    )}
                    </p>
                </div>
                <div>
                  <textarea id='content_txt' name='postDetail' onChange={handleChange} placeholder='내용을 입력하세요.'/>
                </div>
                <div id='post_submit'>
                  <button> 발행 </button>
                </div>
            </form>
          </div>
          <div id='Mains-right'>
          </div>
        </div>
    )
}

export default Write;