package com.example.critichub.data.mock

import com.example.critichub.model.CatalogItem
import com.example.critichub.model.ContentType
import com.example.critichub.model.CriticReview
import com.example.critichub.model.Episode
import com.example.critichub.model.Genre
import com.example.critichub.model.Person
import com.example.critichub.model.Season
import com.example.critichub.model.UserComment
import com.example.critichub.util.fa
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * دادهٔ نمونهٔ مجموعه‌های تلویزیونی (فقط برای نسخهٔ نمایشی).
 * امتیاز قسمت‌ها به‌صورت تعیینی (seed ثابت) تولید می‌شود تا در هر اجرا یکسان بماند.
 */
object MockSeries {

    val all: List<CatalogItem> = listOf(
        s(
            id = "tv-breakingbad",
            titleFa = "بریکینگ بد",
            titleEn = "Breaking Bad",
            year = 2008,
            yearEnd = 2013,
            rating = 9.5,
            votes = 2_100_000,
            genres = g(MockGenres.DRAMA, MockGenres.CRIME, MockGenres.THRILLER),
            trending = true,
            synopsis = "والتر وایت، معلم شیمیِ مبتلا به سرطان، برای تأمین آیندهٔ مالی خانواده‌اش به همراه شاگرد سابقش جسی پینکمن وارد دنیای تولید مت‌آمفتامین می‌شود. اما هر قدم در این مسیر، او را از معلمی آرام به «هایزنبرگ»، پادشاه بی‌رحم مواد، تبدیل می‌کند.",
            director = p("وینس گیلیگان", "Vince Gilligan"),
            cast = cast(
                p("برایان کرانستون", "Bryan Cranston"),
                p("آرون پال", "Aaron Paul"),
                p("آنا گان", "Anna Gunn"),
                p("دین نوریس", "Dean Norris"),
                p("بتسی برنت", "Betsy Brandt")
            ),
            seasons = seasons(
                seed = "breakingbad",
                season(1, 7, 9.0),
                season(2, 9, 8.8),
                season(3, 8, 8.9),
                season(4, 9, 9.3),
                season(5, 9, 9.4)
            ),
            reviews = listOf(
                rev(
                    "آرش محبی", "Arash Mohabi",
                    "آهسته‌سوزترین و بهترین درام تلویزیونی",
                    "«بریکینگ بد» داستان سقوط اخلاقی مردی است که خودش را قهرمان خانواده می‌پندارد. کرانستون با بازی درخشانش، والتر وایت را به یکی از ماندگارترین شخصیت‌های تاریخ تلویزیون تبدیل کرده است.",
                    9.5
                ),
                rev(
                    "نگار جلالی", "Negar Jalaali",
                    "نوشتن شخصیت به سبک وینس گیلیگان",
                    "هر قسمت این سریال مانند یک فیلم سینمایی است؛ از فیلم‌برداری تا موسیقی و دیالوگ‌ها. تحول شخصیت والتر چنان طبیعی و تدریجی است که تماشاگر تا انتها با او هم‌دل می‌ماند.",
                    9.5
                )
            ),
            comments = listOf(
                com("علی محمدی", "۱ روز پیش", "بهترین سریال تاریخ؛ پایان‌بندی اش هیچ‌کس را ناامید نکرد.", 520),
                com("زهرا احمدی", "۱ هفته پیش", "شخصیت پردازی در این سریال یک دورهٔ کامل آموزش فیلمنامه‌نویسی است.", 310),
                com("حسین رضایی", "۲ هفته پیش", "از قسمت اول تا آخر، ول‌کننده نیست!", 288)
            )
        ),

        s(
            id = "tv-got",
            titleFa = "بازی تاج‌وتخت",
            titleEn = "Game of Thrones",
            year = 2011,
            yearEnd = 2019,
            rating = 9.2,
            votes = 2_400_000,
            genres = g(MockGenres.FANTASY, MockGenres.DRAMA, MockGenres.ADVENTURE),
            trending = true,
            synopsis = "هفت‌پادشاهی وستروس برای تصاحب «تخت آهنین» با یکدیگر می‌جنگند، در حالی که در شمال، تهدیدی کهن از پشت دیوار در حال بیدار شدن است. جنگ قدرت، خیانت و سرنوشت خاندان‌های بزرگ، درون‌مایهٔ این حماسهٔ فانتزی است.",
            director = p("دیوید بنیاف", "David Benioff"),
            cast = cast(
                p("پیتر دینکلیج", "Peter Dinklage"),
                p("امیلیا کلارک", "Emilia Clarke"),
                p("کیت هرینگتون", "Kit Harington"),
                p("لینا هیدی", "Lena Headey"),
                p("نیکولای کاستر-والدو", "Nikolaj Coster-Waldau")
            ),
            seasons = seasons(
                seed = "got",
                season(1, 10, 9.0),
                season(2, 10, 9.1),
                season(3, 10, 9.3),
                season(4, 8, 9.2),
                season(5, 8, 8.8)
            ),
            reviews = listOf(
                rev(
                    "امیر کاویانی", "Amir Kavyani",
                    "حماسه‌ای که گفت‌وگوها را به جنگ بدل می‌کند",
                    "«بازی تاج‌وتخت» با شخصیت‌های خاکستری و مرگ‌های غیرمنتظره، قواعد سریال‌های فانتزی را دگرگون کرد. فصل‌های میانی آن، اوج هنر تلویزیون‌سازی‌اند.",
                    9.0
                )
            ),
            comments = listOf(
                com("مریم کاظمی", "۳ روز پیش", "فصل اول تا چهارمش بی‌نظیر است؛ یک تجربهٔ فراموش‌نشدنی.", 431),
                com("محمد صادقی", "۱ هفته پیش", "دنیاسازی و جزئیاتش حیرت‌آور است.", 199)
            )
        ),

        s(
            id = "tv-friends",
            titleFa = "دوستان",
            titleEn = "Friends",
            year = 1994,
            yearEnd = 2004,
            rating = 8.9,
            votes = 1_900_000,
            genres = g(MockGenres.COMEDY, MockGenres.ROMANCE),
            synopsis = "ماجراهای شش دوست ساکن منهتن — راس، مونیکا، چندلر، جویی، فیبی و ریچل — در قهوه‌خانه و آپارتمان‌هایشان؛ داستان عشق‌ها، شغل‌ها و دوستی‌ای که نزدیک‌ترین چیز به خانواده است.",
            director = p("دیوید کرین", "David Crane"),
            cast = cast(
                p("جنیفر آنیستون", "Jennifer Aniston"),
                p("کورتنی کاکس", "Courteney Cox"),
                p("لیزا کودرو", "Lisa Kudrow"),
                p("مت له‌بلانک", "Matt LeBlanc"),
                p("متیو پری", "Matthew Perry"),
                p("دیوید شویمر", "David Schwimmer")
            ),
            seasons = seasons(
                seed = "friends",
                season(1, 12, 8.2),
                season(2, 12, 8.5),
                season(3, 12, 8.4)
            ),
            reviews = listOf(
                rev(
                    "سحر افشار", "Sahar Afshar",
                    "کمدی موقعیتی که هرگز کهنه نمی‌شود",
                    "شیمی میان شش بازیگر اصلی، «دوستان» را به یکی از محبوب‌ترین سریال‌های کمدی تاریخ تبدیل کرده است. دیالوگ‌هایش پس از سه دهه هنوز نقل محافل‌اند.",
                    8.0
                )
            ),
            comments = listOf(
                com("فاطمه نوری", "۲ روز پیش", "هر وقت غمگینم یکی از قسمت‌هایش را می‌بینم؛ همیشه جواب می‌دهد!", 266)
            )
        ),

        s(
            id = "tv-strangerthings",
            titleFa = "چیزهای غریبه",
            titleEn = "Stranger Things",
            year = 2016,
            rating = 8.6,
            votes = 1_400_000,
            genres = g(MockGenres.SCIFI, MockGenres.HORROR, MockGenres.DRAMA),
            trending = true,
            synopsis = "در شهر کوچک هاوکینز در دههٔ هشتاد، ناپدید شدن پسربچه‌ای به نام ویل، گروهی از دوستانش را با دختری مرموز به نام «الون» و دنیایی وارونه در تماس قرار می‌دهد؛ دنیایی تاریک که آرام‌آرام به شهر نفوذ می‌کند.",
            director = p("برادران دافر", "The Duffer Brothers"),
            cast = cast(
                p("وینونا رایدر", "Winona Ryder"),
                p("دیوید هاربر", "David Harbour"),
                p("میلی بابی براون", "Millie Bobby Brown"),
                p("فین ولفهارد", "Finn Wolfhard")
            ),
            seasons = seasons(
                seed = "stranger",
                season(1, 8, 8.7),
                season(2, 9, 8.5),
                season(3, 8, 8.6),
                season(4, 9, 8.8)
            ),
            reviews = listOf(
                rev(
                    "پویا تهرانی", "Pouya Tehrani",
                    "نامهٔ عاشقانه به دههٔ هشتاد",
                    "«چیزهای غریبه» با نوستالژی دههٔ هشتاد و هیجان فراطبیعی، بزرگ و کوچک را کنار هم نشانده است. شخصیت‌های کودک و نوجوانش باورپذیر و دوست‌داشتنی‌اند.",
                    8.5
                )
            ),
            comments = listOf(
                com("سارا موسوی", "۴ روز پیش", "الون یکی از بهترین شخصیت‌های سریال‌های اخیر است.", 245)
            )
        ),

        s(
            id = "tv-lastofus",
            titleFa = "آخرینِ ما",
            titleEn = "The Last of Us",
            year = 2023,
            rating = 8.7,
            votes = 800_000,
            genres = g(MockGenres.DRAMA, MockGenres.ADVENTURE, MockGenres.HORROR),
            trending = true,
            synopsis = "بیست سال پس از فروپاشی تمدن بر اثر قارچی مرگبار، جوئل، قاچاقچی باتجربه، مأمور می‌شود دختر نوجوانی به نام الی را از منطقهٔ قرنطینه خارج کند. الی به عفونت ایمن است و شاید کلید ساخت واکسن و نجات بشریت باشد.",
            director = p("کریگ مازین", "Craig Mazin"),
            cast = cast(
                p("پدرو پاسکال", "Pedro Pascal"),
                p("بلا رمزی", "Bella Ramsey"),
                p("گابریل لونا", "Gabriel Luna"),
                p("آنا تورو", "Anna Torv")
            ),
            seasons = seasons(
                seed = "lastofus",
                season(1, 9, 8.8)
            ),
            reviews = listOf(
                rev(
                    "مینا صادقی", "Mina Sadeghi",
                    "اقتباسی که از بازی ویدیویی هم فراتر رفت",
                    "«آخرینِ ما» درامی عمیق دربارهٔ عشق، فقدان و امید است که اتفاقاً در دنیایی آخرالزمانی روایت می‌شود. پاسکال و رمزی زوجی به‌یادماندنی می‌سازند.",
                    9.0
                )
            ),
            comments = listOf(
                com("نگین عباسی", "۳ روز پیش", "قسمت سومش یک شاهکار مستقل سینمایی بود.", 312)
            )
        ),

        s(
            id = "tv-sherlock",
            titleFa = "شرلوک",
            titleEn = "Sherlock",
            year = 2010,
            yearEnd = 2017,
            rating = 9.1,
            votes = 1_000_000,
            genres = g(MockGenres.CRIME, MockGenres.MYSTERY, MockGenres.DRAMA),
            synopsis = "شرلوک هولمزِ امروزی، کارآگاه نابغهٔ لندن، با دکتر واتسون هم‌خانه می‌شود و پرونده‌های پیچیده‌ای را حل می‌کند؛ در حالی که ذهنِ فوق‌العاده‌اش او را از آدم‌های معمولی جدا و گاه غیرقابل‌تحمل می‌کند.",
            director = p("استیون موفات", "Steven Moffat"),
            cast = cast(
                p("بندیکت کامبربچ", "Benedict Cumberbatch"),
                p("مارتین فریمن", "Martin Freeman"),
                p("یونا استابز", "Una Stubbs"),
                p("روپرت گریوز", "Rupert Graves")
            ),
            seasons = seasons(
                seed = "sherlock",
                season(1, 3, 9.0),
                season(2, 3, 9.3),
                season(3, 3, 8.9),
                season(4, 3, 8.4)
            ),
            reviews = listOf(
                rev(
                    "کیانوش رستمی", "Kianoush Rostami",
                    "نوآوری در روایت کلاسیک کارآگاهی",
                    "به‌روزرسانی هوشمندانهٔ داستان‌های کانن دویل با دیالوگ‌های تند و سریع، «شرلوک» را به پدیده‌ای تلویزیونی تبدیل کرد. کامبربچ، شرلوکی می‌سازد که تا سال‌ها با او مقایسه خواهند شد.",
                    9.0
                )
            ),
            comments = listOf(
                com("آیدا رحیمی", "۵ روز پیش", "سرعت دیالوگ‌ها و منطق داستان، تمرکز کامل می‌خواهد؛ ارزشش را دارد.", 189)
            )
        ),

        s(
            id = "tv-dark",
            titleFa = "دارک",
            titleEn = "Dark",
            year = 2017,
            yearEnd = 2020,
            rating = 8.7,
            votes = 500_000,
            genres = g(MockGenres.SCIFI, MockGenres.MYSTERY, MockGenres.THRILLER),
            synopsis = "ناپدید شدن دو کودک در شهر آلمانی «ویندن»، رازهای تاریک چهار خانواده را در چهار دورهٔ زمانی متفاوت برملا می‌کند. داستانی پیچیده دربارهٔ زمان، تکرار و گره‌هایی که به چند نسل پیش بازمی‌گردد.",
            director = p("باران بو اودار", "Baran bo Odar"),
            cast = cast(
                p("لوئیس هوفمن", "Louis Hofmann"),
                p("لیزا ویکاری", "Lisa Vicari"),
                p("آندریاس پیچمن", "Andreas Pietschman"),
                p("مایا شونه", "Maja Schöne")
            ),
            seasons = seasons(
                seed = "dark",
                season(1, 10, 8.5),
                season(2, 8, 8.6),
                season(3, 8, 8.9)
            ),
            reviews = listOf(
                rev(
                    "الهام صدر", "Elham Sadr",
                    "سفر در زمان با جدیت تمام",
                    "«دارک» معمایی است که از تماشاگرش حداکثر هوش را می‌خواهد. فضای سرد و مالیخولیایی، موسیقی درخور و بازی‌های چندنسلی دقیق، آن را به اثری بی‌نظیر در ژانر خود بدل کرده است.",
                    8.5
                )
            ),
            comments = listOf(
                com("میلاد توکلی", "۲ هفته پیش", "اگر قسمت اول را دوست داشتید، تا آخر سریالش می‌مانید.", 154)
            )
        ),

        s(
            id = "tv-office",
            titleFa = "اداره",
            titleEn = "The Office",
            year = 2005,
            yearEnd = 2013,
            rating = 9.0,
            votes = 900_000,
            genres = g(MockGenres.COMEDY),
            synopsis = "روزمرگی‌های کارمندان شرکت کاغذسازی «داندِر میفلین» به سبک مستند روایت می‌شود؛ با مدیرعامل عجیبی به نام مایکل اسکات که فکر می‌کند بامزه‌ترین فرد جهان است و کارمندانی که هر کدام دنیایی از عجایب دارند.",
            director = p("گرگ دنیلز", "Greg Daniels"),
            cast = cast(
                p("استیو کرل", "Steve Carell"),
                p("رین ویلسون", "Rainn Wilson"),
                p("جان کرازینسکی", "John Krasinski"),
                p("جنا فیشر", "Jenna Fischer")
            ),
            seasons = seasons(
                seed = "office",
                season(1, 6, 8.0),
                season(2, 12, 8.4),
                season(3, 12, 8.3),
                season(4, 9, 8.5)
            ),
            reviews = listOf(
                rev(
                    "ترانه عابدینی", "Taraneh Abedini",
                    "طنزی که از خجالت‌آورترین لحظه‌ها ساخته می‌شود",
                    "«اداره» با قالب مستندنمای خود، کمدی موقعیت را به سطح تازه‌ای برد. استیو کرل در نقش مایکل اسکات، ترکیبی بی‌نظیر از آزاردهندگی و مهر است.",
                    8.5
                )
            ),
            comments = listOf(
                com("بابک نادری", "۱ هفته پیش", "شخصیت دوایت شوت را تا ابد دوست خواهم داشت.", 203)
            )
        )
    )

    // -------------------------------------------------------------- helpers

    private class SeasonSpec(val number: Int, val episodeCount: Int, val baseRating: Double)

    private fun season(number: Int, episodeCount: Int, baseRating: Double) =
        SeasonSpec(number, episodeCount, baseRating)

    private fun seasons(seed: String, vararg specs: SeasonSpec): List<Season> =
        specs.map { spec ->
            val rnd = Random(seed.hashCode() * 31 + spec.number)
            val episodes = (1..spec.episodeCount).map { i ->
                val delta = (rnd.nextInt(17) - 8) / 10.0 // بین ۰٫۸- تا ۰٫۸+
                val rating = ((spec.baseRating + delta) * 10).roundToInt() / 10.0
                Episode(
                    number = i,
                    titleFa = "قسمت ${i.fa()}",
                    rating = rating
                )
            }
            Season(number = spec.number, episodes = episodes)
        }

    private fun s(
        id: String,
        titleFa: String,
        titleEn: String,
        year: Int,
        yearEnd: Int? = null,
        rating: Double,
        votes: Int,
        genres: List<Genre>,
        trending: Boolean = false,
        synopsis: String,
        director: Person,
        cast: List<Person>,
        seasons: List<Season>,
        reviews: List<CriticReview> = emptyList(),
        comments: List<UserComment> = emptyList()
    ) = CatalogItem(
        id = id,
        type = ContentType.SERIES,
        titleFa = titleFa,
        titleEn = titleEn,
        year = year,
        yearEnd = yearEnd,
        genres = genres,
        imdbRating = rating,
        voteCount = votes,
        synopsis = synopsis,
        director = director,
        cast = cast,
        criticReviews = reviews,
        userComments = comments,
        seasons = seasons,
        isTrending = trending
    )

    private fun g(vararg genres: Genre): List<Genre> = genres.toList()

    private fun cast(vararg people: Person): List<Person> = people.toList()
}
