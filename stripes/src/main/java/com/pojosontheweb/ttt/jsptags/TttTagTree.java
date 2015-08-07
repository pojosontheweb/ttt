package com.pojosontheweb.ttt.jsptags;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.DispatcherServlet;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.mock.MockHttpServletRequest;
import net.sourceforge.stripes.mock.MockHttpServletResponse;
import net.sourceforge.stripes.mock.MockServletContext;
import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputTextTag;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TryCatchFinally;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pojosontheweb.ttt.Util.toRtEx;

public class TttTagTree {

    private static void log(String msg) {
        System.out.println(msg);
    }

    private final ServletContext servletContext;
    private final Writer out;
    private final Node root;

    public TttTagTree(ServletContext servletContext, Writer out, Node root) {
        this.servletContext = servletContext;
        this.out = out;
        this.root = root;
    }

    public static class Node {

        private final Tag tag;
        private final Node parent;
        private final List<Node> children;

        public Node(Tag tag, Node parent, List<Node> children) {
            this.tag = tag;
            this.parent = parent;
            this.children = children == null ? new ArrayList<>() : children;
        }

        public Node addChild(Tag child) {
            Node childNode = new Node(child, this, null);
            this.children.add(childNode);
            return childNode;
        }

        @Override
        public String toString() {
            return "Node{" +
                "tag=" + tag +
                '}';
        }
    }

    public void render() {
        render(newPageContext(servletContext, out));
    }

    public void render(TttPageContext context) {
        toRtEx(() -> handleNode(context, root));
    }

    public static TttPageContext newPageContext(ServletContext servletContext, Writer out) {
        TttPageContext pageContext = new TttPageContext();
        MockHttpServletRequest request = new MockHttpServletRequest("skunk", "funk");
        pageContext.setServletContext(servletContext);
        pageContext.setRequest(request);
        pageContext.setResponse(new MockHttpServletResponse());
        pageContext.setJspWriter(new TttJspWriter(out));
        return pageContext;
    }

    public static void handleNode(PageContext pageContext, Node tagNode) throws Exception {
        Tag tag = tagNode.tag;
        tag.setPageContext(pageContext);
        log("handleTag doStartTag " + tag);
        int res = tag.doStartTag();
        try {
            switch (res) {
                case Tag.SKIP_BODY:
                    // don't eval body, call end tag
                    int res2 = tag.doEndTag();
                    switch (res2) {
                        case Tag.SKIP_PAGE:
                            // supposed to skip the rest of the page...
                            log("handleTag SKIP_PAGE " + tag );
                            break;
                        case Tag.EVAL_PAGE:
                            // continue normally
                            log("handleTag EVAL_PAGE " + tag);
                            break;
                    }
                    break;

                case Tag.EVAL_BODY_INCLUDE:
                    // evaluate content
                    log("handleTag EVAL_BODY_INCLUDE" + tag);
                    break;

                case BodyTag.EVAL_BODY_BUFFERED:
                    log("handleTag EVAL_BODY_BUFFERED" + tag);
                    if (!(tag instanceof BodyTag)) {
                        throw new IllegalStateException("impossible");
                    }
                    BodyTag bodyTag = (BodyTag) tag;

                    if (tagNode.children.size()>0) {
                        // push body content
                        BodyContent bodyContent = pageContext.pushBody();
                        bodyTag.setBodyContent(bodyContent);
                        bodyTag.doInitBody();
                        for (Node child : tagNode.children) {

                            //                        log("handleTag handling child " + child.tag + " of " + tagNode.tag + " ...");

                            // initialize nested tag
                            child.tag.setParent(bodyTag);

                            handleNode(pageContext, child);

                            // pop body content

                            //                        log("handleTag ... done handling child " + child.tag);
                        }

                        pageContext.popBody();
                    }

                    int res3 = bodyTag.doAfterBody();
                    switch (res3) {
                        case BodyTag.EVAL_BODY_AGAIN:
                            log("handleTag EVAL_BODY_AGAIN");
                            break;
                        case BodyTag.SKIP_BODY:
                            log("handleTag SKIP_BODY, doEndTag " + bodyTag);
                            bodyTag.doEndTag();
                            break;
                    }

                    break;
            }
        } catch (Throwable e) {
            if (tag instanceof TryCatchFinally) {
                toRtEx(() -> ((TryCatchFinally) tag).doCatch(e));
            }
        } finally {
            if (tag instanceof TryCatchFinally) {
                ((TryCatchFinally)tag).doFinally();
            }
        }


    }

    private static void handleTag(TttPageContext pageContext, Node tagNode, Node parentNode) throws Exception {
        Tag tag = tagNode.tag;
        Tag parent = parentNode != null ? parentNode.tag : null;

        log("handleTag tag=" + tag + ", parent=" + parent);

        // init
        tag.setPageContext(pageContext);
        if (parent != null) {
            // create BodyContent
            BodyContent bodyContent = pageContext.pushBody();

            // initialize nested tag
            tag.setParent(parent);
            BodyTag parentTag = (BodyTag)parent;
            parentTag.setBodyContent(bodyContent);
            parentTag.doInitBody();
        }

        // start tag
        log("handleTag doStartTag " + tag);
        int res = tag.doStartTag();
        try {
            switch (res) {
                case Tag.SKIP_BODY:
                    // don't eval body, call end tag
                    int res2 = tag.doEndTag();
                    switch (res2) {
                        case Tag.SKIP_PAGE:
                            // supposed to skip the rest of the page...
                            log("handleTag " + tag + " SKIP_PAGE");
                            break;
                        case Tag.EVAL_PAGE:
                            // continue normally
                            log("handleTag " + tag + " EVAL_PAGE");
                            break;
                    }
                    break;

                case Tag.EVAL_BODY_INCLUDE:
                    // evaluate content
                    log("handleTag " + tag + " EVAL_BODY_INCLUDE");
                    break;

                case BodyTag.EVAL_BODY_BUFFERED:
                    log("handleTag " + tag + " EVAL_BODY_BUFFERED");
                    if (!(tag instanceof BodyTag)) {
                        throw new IllegalStateException("impossible");
                    }
                    BodyTag bodyTag = (BodyTag) tag;

//                        // todo create nested tags here ?
//                        InputTextTag text = new InputTextTag();
//                        text.setName("myProp");
//                        handleTag(pageContext, text, tag);

                    for (Node child : tagNode.children) {
                        log("handleTag handling child " + child.tag + " of " + tagNode.tag + " ..." );
                        handleTag(pageContext, child, tagNode);
                        log("handleTag ... done handling child " + child.tag);
                    }

                    int res3 = bodyTag.doAfterBody();
                    switch (res3) {
                        case BodyTag.EVAL_BODY_AGAIN:
                            log("handleTag EVAL_BODY_AGAIN");
                            break;
                        case BodyTag.SKIP_BODY:
                            log("handleTag SKIP_BODY, doEndTag " + bodyTag);
                            bodyTag.doEndTag();
                            if (parent!=null) {
                                pageContext.popBody();
                            }
                            break;
                    }

                    break;
            }
        } catch (Throwable e) {
            if (tag instanceof TryCatchFinally) {
                toRtEx(() -> ((TryCatchFinally) tag).doCatch(e));
            }
        } finally {
            if (tag instanceof TryCatchFinally) {
                ((TryCatchFinally)tag).doFinally();
            }
        }
    }

    private static void noop() {
        String s = "";
    }

}
