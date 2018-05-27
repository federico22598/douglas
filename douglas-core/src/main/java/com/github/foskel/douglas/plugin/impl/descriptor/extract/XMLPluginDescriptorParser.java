package com.github.foskel.douglas.plugin.impl.descriptor.extract;

import com.github.foskel.douglas.core.version.Version;
import com.github.foskel.douglas.core.version.Versions;
import com.github.foskel.douglas.core.version.parse.StandardVersionParser;
import com.github.foskel.douglas.core.version.parse.VersionParsingException;
import com.github.foskel.douglas.plugin.descriptor.PluginDescriptor;
import com.github.foskel.douglas.plugin.descriptor.extract.PluginDescriptorParser;
import com.github.foskel.douglas.plugin.descriptor.extract.exception.MissingPluginDataContentException;
import com.github.foskel.douglas.plugin.descriptor.extract.exception.PluginDescriptorParsingException;
import com.github.foskel.douglas.plugin.impl.descriptor.PluginDescriptorBuilder;
import com.github.foskel.douglas.util.JavaListNodeListAdapter;
import com.github.foskel.douglas.util.XmlContentRetriever;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class XMLPluginDescriptorParser implements PluginDescriptorParser {
    private static final String ROOT_ELEMENT_NAME = "plugin";
    private static final String MAIN_CLASS_ELEMENT_NAME = "main";
    private static final String HEADER_ARTIFACT_DESCRIPTOR_ELEMENT_NAME = "descriptor";
    private static final String GROUP_ID_DESCRIPTOR_ELEMENT_NAME = "groupId";
    private static final String ARTIFACT_ID_DESCRIPTOR_ELEMENT_NAME = "artifactId";
    private static final String VERSION_DESCRIPTOR_ELEMENT_NAME = "version";
    private static final String NAME_DESCRIPTOR_ELEMENT_NAME = "name";
    private static final String RESOURCE_TARGETS_ELEMENT_NAME = "resourceTargets";
    private static final String RESOURCE_TARGET_ELEMENT_NAME = "resourceTarget";
    private static final String DEPENDENCIES_ELEMENT_NAME = "dependencies";
    private static final String DEPENDENCIES_DEPENDENCY_ELEMENT_NAME = "dependency";

    private static Document parseDocument(URL url) throws PluginDescriptorParsingException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;

        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new PluginDescriptorParsingException("Unable to parse the contents from the plugin " +
                    "data file URL \"" + url.toString() + "\":", e);
        }

        InputStream dataFileStream;

        try {
            dataFileStream = url.openStream();
        } catch (IOException e) {
            throw new PluginDescriptorParsingException("Unable to open a valid InputStream from the " +
                    "plugin data file URL \"" + url.toString() + "\":", e);
        }

        Document document;

        try {
            document = documentBuilder.parse(dataFileStream);
        } catch (SAXException | IOException e) {
            throw new PluginDescriptorParsingException("Unable to parse the contents from the plugin " +
                    "data file URL \"" + url.toString() + "\":", e);
        }

        return document;
    }

    private static Element extractRootElement(Document document,
                                              URL dataFileURL) throws PluginDescriptorParsingException {
        Element rootElement = document.getDocumentElement();

        if (!rootElement.getNodeName().equals(ROOT_ELEMENT_NAME)) {
            throw new PluginDescriptorParsingException("Root XML element of data file URL " +
                    "\"" + dataFileURL.toString() + "\" does not match " +
                    "\" " + ROOT_ELEMENT_NAME + "\".");
        }

        return rootElement;
    }

    private static PluginDescriptorBuilder extractDescriptor(JavaListNodeListAdapter descriptorNodes,
                                                             URL dataFileURL) {
        if (descriptorNodes.isEmpty()) {
            throw new MissingPluginDataContentException("Root element of plugin data file URL " +
                    "\"" + dataFileURL.toString() + "\" is empty.");
        }

        PluginDescriptorBuilder informationBuilder = new PluginDescriptorBuilder();

        descriptorNodes.stream()
                .filter(Objects::nonNull)
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .map(Element.class::cast)
                .forEach(node -> {
                    String groupId = extractNodeAsText(node,
                            GROUP_ID_DESCRIPTOR_ELEMENT_NAME);
                    String artifactId = extractNodeAsText(node,
                            ARTIFACT_ID_DESCRIPTOR_ELEMENT_NAME);
                    Version version = extractVersion(node);
                    String name = extractNodeAsText(node,
                            NAME_DESCRIPTOR_ELEMENT_NAME);

                    informationBuilder
                            .withGroupId(groupId)
                            .withArtifactId(artifactId)
                            .withVersion(version)
                            .withName(name);
                });

        return informationBuilder;
    }

    private static Version extractVersion(Element element) {
        String versionAsString = extractNodeAsText(element,
                VERSION_DESCRIPTOR_ELEMENT_NAME);

        try {
            return Versions.of(versionAsString);
        } catch (VersionParsingException e) {
            e.printStackTrace();

            return Version.MIN_VERSION;
        }
    }

    private static String extractMainClass(Document document, URL dataFileURL) {
        Optional<String> nodeTextResult = XmlContentRetriever.findFirstAsText(document,
                MAIN_CLASS_ELEMENT_NAME);

        return nodeTextResult.orElseThrow(() -> new MissingPluginDataContentException(
                "Missing required node (" + MAIN_CLASS_ELEMENT_NAME + ") from plugin data file URL " +
                        "\"" + dataFileURL.toString() + "\"."));
    }

    private static List<String> extractResourceTargets(JavaListNodeListAdapter nodes) {
        if (nodes.isEmpty()) {
            return Collections.emptyList();
        }

        return nodes.stream()
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .map(XMLPluginDescriptorParser::extractResourceTarget)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static String extractResourceTarget(Node node) {
        Element element = (Element) node;

        return XmlContentRetriever.findFirstAsText(element, RESOURCE_TARGET_ELEMENT_NAME).orElse(null);
    }

    private static List<PluginDescriptor> extractDependencyDescriptors(JavaListNodeListAdapter nodes, URL dataFileURL) {
        if (nodes.isEmpty()) {
            return Collections.emptyList();
        }

        return nodes.stream()
                .filter(node -> node.getNodeType() == Node.ELEMENT_NODE)
                .map(XMLPluginDescriptorParser::extractDependencyElementNodes)
                .flatMap(list -> extractDependencyDescriptorNodes(list, dataFileURL))
                .collect(Collectors.toList());
    }

    private static JavaListNodeListAdapter extractDependencyElementNodes(Node node) {
        Element element = (Element) node;
        NodeList dependencyNodeElements = element.getElementsByTagName(
                DEPENDENCIES_DEPENDENCY_ELEMENT_NAME);

        return new JavaListNodeListAdapter(dependencyNodeElements);
    }

    private static Stream<PluginDescriptor> extractDependencyDescriptorNodes(
            JavaListNodeListAdapter descriptorNodes,
            URL dataFileURL) {
        if (descriptorNodes.isEmpty()) {
            throw new MissingPluginDataContentException("There is an empty " +
                    "dependency descriptor for the plugin data file URL " +
                    "\"" + dataFileURL.toString() + "\".");
        }

        return descriptorNodes.stream()
                .filter(Objects::nonNull)
                .filter(descriptorNode -> descriptorNode.getNodeType() == Node.ELEMENT_NODE)
                .map(Element.class::cast)
                .map(XMLPluginDescriptorParser::extractDependencyDescriptor);
    }

    private static PluginDescriptor extractDependencyDescriptor(Element element) {
        String groupId = extractNodeAsText(element,
                GROUP_ID_DESCRIPTOR_ELEMENT_NAME);

        String artifactId = extractNodeAsText(element,
                ARTIFACT_ID_DESCRIPTOR_ELEMENT_NAME);

        Version version = extractDependencyVersion(element);
        String name = extractDependencyName(element);

        return new PluginDescriptorBuilder()
                .withGroupId(groupId)
                .withArtifactId(artifactId)
                .withVersion(version)
                .withName(name)

                .build();
    }

    private static Version extractDependencyVersion(Element element) {
        Optional<String> textNodeResult = XmlContentRetriever.findFirstAsText(element,
                VERSION_DESCRIPTOR_ELEMENT_NAME);

        if (!textNodeResult.isPresent()) {
            return null;
        }

        String textNode = textNodeResult.get();

        try {
            return StandardVersionParser.INSTANCE.parse(textNode);
        } catch (VersionParsingException e) {
            e.printStackTrace();

            return null;
        }
    }

    private static String extractDependencyName(Element element) {
        Optional<String> textNodeResult = XmlContentRetriever.findFirstAsText(element,
                NAME_DESCRIPTOR_ELEMENT_NAME);
        String defaultNameElement = "";

        return textNodeResult.orElse(defaultNameElement);
    }

    private static String extractNodeAsText(Element element, String name) {
        Optional<String> textNodeResult = XmlContentRetriever.findFirstAsText(element, name);

        return textNodeResult.orElseThrow(() -> new MissingPluginDataContentException(
                "Missing required node (" + name + ") from plugin data file element " +
                        "\"" + element.toString() + "\"."));
    }

    @Override
    public PluginDescriptor parse(URL dataFileURL) throws PluginDescriptorParsingException {
        Document document = parseDocument(dataFileURL);
        Element rootElement = extractRootElement(document, dataFileURL);

        rootElement.normalize();

        JavaListNodeListAdapter artifactDescriptorNodes = new JavaListNodeListAdapter(
                document.getElementsByTagName(HEADER_ARTIFACT_DESCRIPTOR_ELEMENT_NAME));
        PluginDescriptorBuilder descriptorBuilder = extractDescriptor(
                artifactDescriptorNodes,
                dataFileURL);

        String mainClassName = extractMainClass(document, dataFileURL);
        NodeList resourceTargetNodes = document.getElementsByTagName(RESOURCE_TARGETS_ELEMENT_NAME);
        List<String> resourceTargets = extractResourceTargets(
                new JavaListNodeListAdapter(resourceTargetNodes));

        NodeList dependencyNodes = document.getElementsByTagName(DEPENDENCIES_ELEMENT_NAME);
        List<PluginDescriptor> dependencyDescriptors = extractDependencyDescriptors(
                new JavaListNodeListAdapter(dependencyNodes),
                dataFileURL);

        return descriptorBuilder
                .withClassName(mainClassName)
                .addDependencyDescriptors(dependencyDescriptors)
                .addResourceTargets(resourceTargets)

                .build();
    }
}