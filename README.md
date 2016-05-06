# Egocentric Video Indexing and Summarization

The project aims to summarize and index a video shot on a GoPro or any mounted camera (an "Egocentric" video) so as to reduce
the unwanted monotonous imagery that often take up most of the video time. 

When shooting using a GoPro, the video captured are the events during the entire duration whenever the camera is ON. This creates a 
lot of redundancy wasting a lot of space on the video capturing frames with no information content. 

The <b>summarizer</b> reduces this redundant content in the video by cutting out the unwanted parts and only keeping the sections with 
high information content in the final video. 

The <b>indexer</b> clusters frames in buckets to allow for fast image searching. You provide the program with a sample image and it will
try to find the image in the video and present with a subsection of the video that contains that image or an image similar to the
sample image.


The project is an IntelliJIDEA project and uses OpenCV 3.1.0
