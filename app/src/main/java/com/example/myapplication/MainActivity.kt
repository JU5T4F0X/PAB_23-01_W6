package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

// ── Data Model ──────────────────────────────────────────────────────────────

data class ArchiePost(
    val id: Int,
    val imageRes: Int,
    val title: String,
    val category: String,
    val heightFactor: Float = 1f,
)

val samplePosts = listOf(
    ArchiePost(1,  R.drawable.photo_forest,          "Into the Wild Forest",   "Nature",      1.3f),
    ArchiePost(2,  R.drawable.photo_river,           "Crystal River at Dusk",  "Landscape",   1.0f),
    ArchiePost(3,  R.drawable.photo_stars,           "Starry Night Escape",    "Astronomy",   1.5f),
    ArchiePost(4,  R.drawable.photo_cave,            "Hidden Cave Wonder",     "Adventure",   1.2f),
    ArchiePost(5,  R.drawable.photo_mist,            "Morning Mist Valley",    "Nature",      1.0f),
    ArchiePost(6,  R.drawable.photo_mountains,       "Majestic Mountains",     "Landscape",   1.4f),
    ArchiePost(7,  R.drawable.photo_lake,            "Mirror Lake Reflection", "Nature",      1.1f),
    ArchiePost(8,  R.drawable.photo_coast,           "Coastal Serenity",       "Travel",      1.3f),
    ArchiePost(9,  R.drawable.photo_sunset,          "Golden Hour Sunset",     "Photography", 1.0f),
    ArchiePost(10, R.drawable.photo_autumn,          "Autumn Pond Serenity",   "Seasons",     1.2f),
    ArchiePost(11, R.drawable.photo_pixel,           "Pixel Art Village",      "Art",         1.5f),
    ArchiePost(12, R.drawable.photo_mountain_sunset, "Peak Glow at Dusk",      "Landscape",   1.1f),
    ArchiePost(13, R.drawable.photo_green,           "Lush Green Meadow",      "Nature",      1.3f),
    ArchiePost(14, R.drawable.photo_city,            "City Lights and Fog",    "Urban",       1.0f),
    ArchiePost(15, R.drawable.photo_road,            "Road Less Travelled",    "Travel",      1.4f),
    ArchiePost(16, R.drawable.photo_snow,            "Snowy Peaks Wonder",     "Winter",      1.2f),
)

// ── Color Palette ────────────────────────────────────────────────────────────

val BgDark        = Color(0xFF0D0D0D)
val SurfaceDark   = Color(0xFF1A1A1A)
val CardDark      = Color(0xFF222222)
val AccentRose    = Color(0xFFE91E63)
val AccentGold    = Color(0xFFFFD54F)
val TextPrimary   = Color(0xFFF5F5F5)
val TextSecondary = Color(0xFF9E9E9E)
val NavBarColor   = Color(0xFF141414)

// ── Nav Item helper ──────────────────────────────────────────────────────────

data class NavItem(val screen: Screen, val icon: ImageVector, val label: String)

// ── Activity ─────────────────────────────────────────────────────────────────

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ArchievyApp()
            }
        }
    }
}

// ── App Shell ─────────────────────────────────────────────────────────────────

enum class Screen { HOME, FAVORITES, PROFILE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchievyApp() {
    var currentScreen by rememberSaveable { mutableStateOf(Screen.HOME) }
    var likedIds by rememberSaveable { mutableStateOf(setOf<Int>()) }

    val topBarTitle = when (currentScreen) {
        Screen.HOME      -> "Archievy"
        Screen.FAVORITES -> "Favorites"
        Screen.PROFILE   -> "Profile"
    }

    Scaffold(
        containerColor = BgDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topBarTitle,
                        fontSize = if (currentScreen == Screen.HOME) 26.sp else 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (currentScreen == Screen.HOME) AccentRose else TextPrimary,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NavBarColor),
            )
        },
        bottomBar = {
            ArchievyBottomNav(
                current = currentScreen,
                onSelect = { currentScreen = it },
                favCount = likedIds.size,
            )
        }
    ) { innerPadding ->
        when (currentScreen) {
            Screen.HOME ->
                HomeScreen(
                    posts = samplePosts,
                    likedIds = likedIds,
                    onToggleLike = { id ->
                        likedIds = if (id in likedIds) likedIds - id else likedIds + id
                    },
                    modifier = Modifier.padding(innerPadding),
                )
            Screen.FAVORITES ->
                FavoritesScreen(
                    posts = samplePosts.filter { it.id in likedIds },
                    likedIds = likedIds,
                    onToggleLike = { id ->
                        likedIds = if (id in likedIds) likedIds - id else likedIds + id
                    },
                    modifier = Modifier.padding(innerPadding),
                )
            Screen.PROFILE ->
                ProfileScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

// ── Bottom Navigation ─────────────────────────────────────────────────────────

@Composable
fun ArchievyBottomNav(
    current: Screen,
    onSelect: (Screen) -> Unit,
    favCount: Int,
) {
    val navItems = listOf(
        NavItem(Screen.HOME,      Icons.Filled.Home,     "Home"),
        NavItem(Screen.FAVORITES, Icons.Filled.Favorite, "Favorites"),
        NavItem(Screen.PROFILE,   Icons.Filled.Person,   "Profile"),
    )

    NavigationBar(
        containerColor = NavBarColor,
        tonalElevation = 0.dp,
    ) {
        navItems.forEach { navItem ->
            val selected = current == navItem.screen
            NavigationBarItem(
                selected = selected,
                onClick = { onSelect(navItem.screen) },
                icon = {
                    Box {
                        Icon(
                            imageVector = navItem.icon,
                            contentDescription = navItem.label,
                            modifier = Modifier.size(24.dp),
                        )
                        if (navItem.screen == Screen.FAVORITES && favCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.TopEnd)
                                    .background(AccentRose, CircleShape),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = if (favCount > 9) "9+" else favCount.toString(),
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                },
                label = {
                    Text(
                        text = navItem.label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = AccentRose,
                    selectedTextColor   = AccentRose,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor      = SurfaceDark,
                ),
            )
        }
    }
}

// ── Home Screen ───────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    posts: List<ArchiePost>,
    likedIds: Set<Int>,
    onToggleLike: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxSize().background(BgDark),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp,
    ) {
        items(posts, key = { it.id }) { post ->
            PostCard(
                post = post,
                isLiked = post.id in likedIds,
                onToggleLike = { onToggleLike(post.id) },
            )
        }
    }
}

// ── Post Card ─────────────────────────────────────────────────────────────────

@Composable
fun PostCard(
    post: ArchiePost,
    isLiked: Boolean,
    onToggleLike: () -> Unit,
) {
    val cardHeight = (180 * post.heightFactor).dp

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = post.imageRes),
                contentDescription = post.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xCC000000))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 10.dp, bottom = 10.dp, end = 36.dp),
            ) {
                Text(
                    text = post.title,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 15.sp,
                )
                Text(
                    text = post.category,
                    color = AccentGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            LikeButton(
                isLiked = isLiked,
                onClick = onToggleLike,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
            )
        }
    }
}

// ── Animated Like Button ──────────────────────────────────────────────────────

@Composable
fun LikeButton(
    isLiked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue = if (isLiked) 1.3f else 1.0f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 400f),
        label = "like_scale",
    )
    val tint by animateColorAsState(
        targetValue = if (isLiked) AccentRose else Color.White,
        label = "like_tint",
    )
    val likeIcon = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder

    Box(
        modifier = modifier
            .size(32.dp)
            .background(Color(0x66000000), CircleShape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = likeIcon,
            contentDescription = "Like",
            tint = tint,
            modifier = Modifier
                .size(18.dp)
                .scale(scale),
        )
    }
}

// ── Favorites Screen ──────────────────────────────────────────────────────────

@Composable
fun FavoritesScreen(
    posts: List<ArchiePost>,
    likedIds: Set<Int>,
    onToggleLike: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (posts.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize().background(BgDark),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val emptyIcon = Icons.Filled.FavoriteBorder
                Icon(
                    imageVector = emptyIcon,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(72.dp),
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "No favorites yet",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Tap the heart on any photo to save it here",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = modifier.fillMaxSize().background(BgDark),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalItemSpacing = 10.dp,
        ) {
            items(posts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    isLiked = post.id in likedIds,
                    onToggleLike = { onToggleLike(post.id) },
                )
            }
        }
    }
}

// ── Profile Screen ────────────────────────────────────────────────────────────

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.photo_mountains),
                contentDescription = "Profile banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0x44000000), Color(0xCC000000))
                        )
                    )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(AccentRose, Color(0xFF9C27B0)))
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "JF",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "JustAFox",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
            )

            Text(
                text = "@justafox",
                color = AccentRose,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Capturing moments, chasing light.\nNature & urban explorer",
                color = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 32.dp),
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .background(SurfaceDark, RoundedCornerShape(16.dp))
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                ProfileStat(value = "${samplePosts.size}", label = "Posts")
                ProfileStatDivider()
                ProfileStat(value = "1.2K", label = "Followers")
                ProfileStatDivider()
                ProfileStat(value = "348", label = "Following")
            }

            Spacer(Modifier.height(24.dp))

            ProfileInfoCard()
        }
    }
}

@Composable
fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 12.sp,
        )
    }
}

@Composable
fun ProfileStatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(36.dp)
            .background(Color(0xFF333333))
    )
}

@Composable
fun ProfileInfoCard() {
    val items = listOf(
        Pair("📍", "Somewhere, Earth"),
        Pair("🎨", "Photography · Nature · Art"),
        Pair("🔗", "archievy.app/justafox"),
        Pair("📅", "Joined October 2024"),
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = item.first, fontSize = 16.sp)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = item.second,
                        color = TextSecondary,
                        fontSize = 13.sp,
                    )
                }
                if (index < items.lastIndex) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(1.dp)
                            .background(Color(0xFF2C2C2C))
                    )
                }
            }
        }
    }
}
