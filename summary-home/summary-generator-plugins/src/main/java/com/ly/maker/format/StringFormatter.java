package com.ly.maker.format;

import com.ly.maker.enums.NodeType;
import com.ly.maker.model.ChildNode;
import com.ly.maker.model.Node;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The type String formatter.
 * @author zhangliang
 * @version Id : StringFormatter, v 0.1 2018/5/3 16:49 zhangliang Exp $
 */
public class StringFormatter implements Formatter<Node, List<String>> {
    @Override
    public List<String> format(Node node) {
        List<String> strings = new ArrayList<>();
        initHeader(strings);
        initBody(node, strings, 0);
        return strings;
        //return String.join("\r\n", strings);
    }

    private void initBody(Node node, List<String> strings, int spaceCount) {
        List<Node> nodes = node.getNodes();
        if (nodes == null || nodes.size() == 0) {
            return;
        }

        for (Node nodeItem : nodes) {
            String str = getSpace(spaceCount) + nodeItem.format();
            if (!nodeItem.getNodeType().equals(NodeType.Parent)) {
                // System.out.println(str);
                strings.add(str);
            }

            if (nodeItem.getNodes() != null && nodeItem.getNodes().size() != 0) {
                initBody(nodeItem, strings, spaceCount + 2);
            }
        }
    }

    private String getSpace(int size) {
        StringBuilder str = new StringBuilder();
        if (size <= 0) {
            return str.toString();
        }

        for (int i = 0; i <= size; i++) {
            str.append(" ");
        }

        return str.toString() + "|";
    }

    private void initHeader(List<String> strings) {
        strings.add("属性名\t数据类型\t是否必须\t参数说明11");
    }
}
