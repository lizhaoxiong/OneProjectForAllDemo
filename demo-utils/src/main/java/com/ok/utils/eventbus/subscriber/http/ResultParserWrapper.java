package com.ok.utils.eventbus.subscriber.http;


import com.ok.utils.utils.data.ProtocolParser;

import org.json.JSONObject;

/**
 * 处理服务器是否成功返回 state 0 成功
 */
public class ResultParserWrapper extends IResultParser<JSONObject> {
    private IResultParser mInnerParser;

    public ResultParserWrapper(IResultParser innerParser) {
        this.mInnerParser = innerParser;
    }

    @Override
    public void parse(JSONObject result) throws Exception {
        int state = ProtocolParser.getJsonInt(result, "state");
        if (state != 0 || !result.has("content")) {
            if (result.has("msg")) {
                mInnerParser.setErrorObject(ErrorObject.build(state, ProtocolParser.getJsonStr(result, "message")));
            }
            else {
                mInnerParser.setErrorObject(ErrorObject.build(state));
            }
            return;
        }
        result = ProtocolParser.getJsonObj(result, "content");
        if (mInnerParser != null) {
            mInnerParser.parse(result);
        }
    }

}
